package xoric.prism.develop.meta;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;
import xoric.prism.global.PrismGlobal;
import xoric.prism.swing.PrismFrame;

public class MetaFileManager extends PrismFrame implements MouseListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	private final static IPath_r resPath = new Path("E:/Prism/resource");

	private final JTree tree;
	private final DefaultMutableTreeNode root;

	private JToolBar toolBar;
	private JButton reloadButton;
	private JButton expandButton;
	private JButton collapseButton;
	private JButton createButton;
	private JButton createAllButton;

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
		toolBar.addSeparator();
		toolBar.add(createButton = createButton("Create selected"));
		toolBar.addSeparator();
		toolBar.add(createAllButton = createButton("Create all"));

		// tree
		root = new DefaultMutableTreeNode("Resources");
		tree = new JTree(root);
		tree.addMouseListener(this);
		JScrollPane scroll = new JScrollPane(tree);

		// content panel
		JPanel p = new JPanel(new BorderLayout());
		p.add(BorderLayout.NORTH, toolBar);
		p.add(BorderLayout.CENTER, scroll);

		this.setContentPane(p);
	}

	private JButton createButton(String s)
	{
		JButton b = new JButton(s);
		b.addActionListener(this);
		return b;
	}

	private void tryLoadDir(boolean autoCreate)
	{
		try
		{
			loadDir(autoCreate);
			onExpandAll();
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	public void loadDir(boolean autoCreate) throws PrismException
	{
		if (autoCreate)
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		root.removeAllChildren();
		loadSubdirs(resPath, root, autoCreate);
		tree.updateUI();

		if (autoCreate)
			setCursor(Cursor.getDefaultCursor());
	}

	private boolean loadSubdirs(IPath_r path, DefaultMutableTreeNode parent, boolean autoCreate) throws PrismException
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
						creator.create();
					}
				}
				else
					ok = loadSubdirs(subPath, node, autoCreate);

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
		}
	}

	private static void createMetaFile(String subdir, boolean showConfirmation)
	{
		MetaFileCreator creator = new MetaFileCreator(new Path(resPath.getFile(subdir)), Prism.global.getDataPath());
		try
		{
			creator.create();
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
		tryLoadDir(false);
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

	private void onCreateAll()
	{
		int result = JOptionPane.showConfirmDialog(null, "Please confirm creating all resources.", "Create all", JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION)
			tryLoadDir(true);
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
			onCreateAll();
	}

	public static void main(String[] args)
	{
		try
		{
			// global initialization
			PrismGlobal.setLookAndFeel();
			PrismGlobal global = new PrismGlobal();
			global.load();
			Prism.global = global;

			// initialize
			PrismDataLoader.loadAll();

			MetaFileManager m = new MetaFileManager();
			m.setVisible(true);
			m.onReload();
		}
		catch (PrismException e)
		{
			e.user.showMessage();
			e.code.print();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
