package xoric.prism.develop.meta;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;
import xoric.prism.global.PrismGlobal;
import xoric.prism.swing.PrismFrame;
import xoric.prism.swing.tooltips.ToolTipFormatter;

public class MetaFileManager extends PrismFrame implements MouseListener, ActionListener, TreeSelectionListener
{
	private static final long serialVersionUID = 1L;
	private static IPath_r resPath;

	private final JTree tree;
	private final DefaultMutableTreeNode root;

	private final JToolBar toolBar;
	private final JToolBar toolBar2;
	private final JButton reloadButton;
	private final JButton expandButton;
	private final JButton collapseButton;
	private final JButton createButton;
	private final JButton createAllButton;
	private final JButton recreateAllButton;

	private MetaContentPanel metaContentPanel;

	public MetaFileManager()
	{
		super("MetaFile Manager", 640, 400, true);

		// button toolbar
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(reloadButton = createButton("Reload"));
		toolBar.addSeparator();
		toolBar.add(expandButton = createButton("Expand all"));
		toolBar.addSeparator();
		toolBar.add(collapseButton = createButton("Collapse all"));

		toolBar2 = new JToolBar();
		toolBar2.setFloatable(false);
		toolBar2.add(createButton = createButton("Create selected"));
		toolBar2.addSeparator();
		toolBar2.add(createAllButton = createButton("Create all"));
		toolBar2.addSeparator();
		toolBar2.add(recreateAllButton = createButton("Recreate all"));

		createAllButton.setToolTipText(ToolTipFormatter.split("Create all files and increase their versions by one."));
		recreateAllButton.setToolTipText(ToolTipFormatter.split("Recreate all files and set their versions to zero."));

		// MetaContentPanel
		metaContentPanel = new MetaContentPanel();

		// tree
		root = new DefaultMutableTreeNode("Resources");
		tree = new JTree(root);
		tree.addMouseListener(this);
		tree.addTreeSelectionListener(this);
		JScrollPane scroll = new JScrollPane(tree);

		// content panel
		JPanel p = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.6, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0);
		p.add(toolBar, c);

		c = new GridBagConstraints(0, 1, 1, 1, 0.6, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
				0);
		p.add(toolBar2, c);

		c = new GridBagConstraints(0, 2, 1, 1, 0.6, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		p.add(scroll, c);

		c = new GridBagConstraints(1, 0, 1, 3, 0.4, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		p.add(metaContentPanel, c);

		this.setContentPane(p);
	}

	private JButton createButton(String s)
	{
		JButton b = new JButton(s);
		b.addActionListener(this);
		return b;
	}

	private void tryLoadDir(boolean autoCreate, boolean resetVersions)
	{
		try
		{
			loadDir(autoCreate, resetVersions);
			onExpandAll();
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	public void loadDir(boolean autoCreate, boolean resetVersions) throws PrismException
	{
		if (autoCreate)
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		root.removeAllChildren();
		loadSubdirs(resPath, root, autoCreate, resetVersions);
		tree.updateUI();

		if (autoCreate)
			setCursor(Cursor.getDefaultCursor());
	}

	private boolean loadSubdirs(IPath_r path, DefaultMutableTreeNode parent, boolean autoCreate, boolean resetVersions)
			throws PrismException
	{
		boolean found = false;
		File[] subDirs = path.listFiles();

		for (File file : subDirs)
		{
			if (file.isDirectory())
			{
				Path subPath = new Path(file);
				String s = file.getName();
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(s);
				boolean ok;

				if (subPath.getFile("meta.txt").exists())
				{
					ok = true;
					if (autoCreate)
					{
						MetaFileCreator creator = new MetaFileCreator(subPath, Prism.global.getDataPath());
						creator.create(resetVersions);
					}
				}
				else
					ok = loadSubdirs(subPath, node, autoCreate, resetVersions);

				if (ok)
				{
					found = true;
					parent.add(node);
				}
			}
		}
		return found;
	}

	private void onCreateSelected()
	{
		TreePath[] paths = tree.getSelectionPaths();
		int n = paths != null ? paths.length : 0;

		if (n > 0)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[paths.length - 1].getLastPathComponent();
			String path = "";

			int childCount = node.getChildCount();
			if (childCount == 0)
			{
				for (int i = 0; i < paths.length; ++i)
				{
					int count = paths[i].getPathCount();
					for (int j = 1; j < count; ++j)
					{
						node = (DefaultMutableTreeNode) paths[i].getPathComponent(j);
						path = path + node.toString();
						if (j < count - 1)
							path = path + File.separator;
					}
				}
			}
			createMetaFile(path, true);
			metaContentPanel.showContent(new Path(resPath.getFile(path)));
		}
	}

	private static void createMetaFile(String subdir, boolean showConfirmation)
	{
		MetaFileCreator creator = new MetaFileCreator(new Path(resPath.getFile(subdir)), Prism.global.getDataPath());
		try
		{
			creator.create(false);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getClickCount() == 2)
			onCreateSelected();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
	}

	private void onReload()
	{
		tryLoadDir(false, false);
	}

	private void onExpandAll()
	{
		for (int i = 0; i < tree.getRowCount(); i++)
			tree.expandRow(i);
	}

	private void onCollapseAll()
	{
		for (int i = 1; i < tree.getRowCount(); i++)
			tree.collapseRow(i);
	}

	private void onCreateAll(boolean resetVersions)
	{
		int result = JOptionPane.showConfirmDialog(null, "Please confirm creating all resources.", "Create all", JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION)
			tryLoadDir(true, resetVersions);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if (source == reloadButton)
			onReload();
		else if (source == expandButton)
			onExpandAll();
		else if (source == collapseButton)
			onCollapseAll();
		else if (source == createButton)
			onCreateSelected();
		else if (source == createAllButton)
			onCreateAll(false);
		else if (source == recreateAllButton)
			onCreateAll(true);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		TreePath[] paths = tree.getSelectionPaths();
		int n = paths != null ? paths.length : 0;

		if (n > 0)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[paths.length - 1].getLastPathComponent();
			int childCount = node.getChildCount();

			if (childCount == 0)
			{
				String p = "";

				for (int i = 0; i < paths.length; ++i)
				{
					int count = paths[i].getPathCount();
					for (int j = 1; j < count; ++j)
					{
						node = (DefaultMutableTreeNode) paths[i].getPathComponent(j);
						p = p + node.toString();
						if (j < count - 1)
							p = p + '/';
					}
				}
				Path resPath = new Path(this.resPath.getFile(p).toString());
				metaContentPanel.showContent(resPath);
			}
		}
	}

	public static void main(String[] args)
	{
		//		try
		//		{
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");

		String s;
		if (os.equals("Linux"))
			s = "/home/xoric/workspace/resource";
		else
			s = "E:/Prism/resource";
		resPath = new Path(s);

		// global initialization
		PrismGlobal.setLookAndFeel();
		PrismGlobal global = new PrismGlobal();
		global.init();
		//			global.load();
		Prism.global = global;

		// initialize
		//			PrismDataLoader.loadAll();

		MetaFileManager m = new MetaFileManager();
		m.setVisible(true);
		m.onReload();
		//		}
		//		catch (PrismException e)
		//		{
		//			e.user.showMessage();
		//			e.code.print();
		//		}
		//		catch (Exception e)
		//		{
		//			e.printStackTrace();
		//		}
	}

}
