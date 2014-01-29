package xoric.prism.develop.meta;

/**
 * =================================================== This program contains code from the book "Swing" 2nd Edition by Matthew Robinson and
 * Pavel Vorobiev http://www.spindoczine.com/sbe ===================================================
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class DirTree extends JFrame
{
	public static final String APP_NAME = "Directories Tree";
	public static final ImageIcon ICON_COMPUTER = new ImageIcon("computer.gif");
	public static final ImageIcon ICON_DISK = new ImageIcon("disk.gif");
	public static final ImageIcon ICON_FOLDER = new ImageIcon("folder.gif");
	public static final ImageIcon ICON_EXPANDEDFOLDER = new ImageIcon("expandedfolder.gif");
	protected JTree m_tree;
	protected DefaultTreeModel m_model;
	protected JTextField m_display;

	public DirTree()
	{
		super(APP_NAME);
		setSize(400, 300);
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(new IconData(ICON_COMPUTER, null, "Computer"));
		DefaultMutableTreeNode node;
		File[] roots = File.listRoots();
		for (int k = 0; k < roots.length; k++)
		{
			node = new DefaultMutableTreeNode(new IconData(ICON_DISK, null, new FileNode(roots[k])));
			top.add(node);
			node.add(new DefaultMutableTreeNode(new Boolean(true)));
		}
		m_model = new DefaultTreeModel(top);
		m_tree = new JTree(m_model);
		m_tree.putClientProperty("JTree.lineStyle", "Angled");
		IconCellRenderer renderer = new IconCellRenderer();
		m_tree.setCellRenderer(renderer);
		m_tree.addTreeExpansionListener(new DirExpansionListener());
		m_tree.addTreeSelectionListener(new DirSelectionListener());
		m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		m_tree.setShowsRootHandles(true);
		m_tree.setEditable(false);
		JScrollPane s = new JScrollPane();
		s.getViewport().add(m_tree);
		getContentPane().add(s, BorderLayout.CENTER);
		m_display = new JTextField();
		m_display.setEditable(false);
		m_display.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		getContentPane().add(m_display, BorderLayout.NORTH);
	}

	DefaultMutableTreeNode getTreeNode(TreePath path)
	{
		return (DefaultMutableTreeNode) (path.getLastPathComponent());
	}

	FileNode getFileNode(DefaultMutableTreeNode node)
	{
		if (node == null)
			return null;
		Object obj = node.getUserObject();
		if (obj instanceof IconData)
			obj = ((IconData) obj).getObject();
		if (obj instanceof FileNode)
			return (FileNode) obj;
		else
			return null;
	}

	// Make sure expansion is threaded and updating the tree model
	// only occurs within the event dispatching thread.
	class DirExpansionListener implements TreeExpansionListener
	{
		@Override
		public void treeExpanded(TreeExpansionEvent event)
		{
			final DefaultMutableTreeNode node = getTreeNode(event.getPath());
			final FileNode fnode = getFileNode(node);
			Thread runner = new Thread()
			{
				@Override
				public void run()
				{
					if (fnode != null && fnode.expand(node))
					{
						Runnable runnable = new Runnable()
						{
							@Override
							public void run()
							{
								m_model.reload(node);
							}
						};
						SwingUtilities.invokeLater(runnable);
					}
				}
			};
			runner.start();
		}

		@Override
		public void treeCollapsed(TreeExpansionEvent event)
		{
		}
	}

	class DirSelectionListener implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent event)
		{
			DefaultMutableTreeNode node = getTreeNode(event.getPath());
			FileNode fnode = getFileNode(node);
			if (fnode != null)
				m_display.setText(fnode.getFile().getAbsolutePath());
			else
				m_display.setText("");
		}
	}

	public static void main(String argv[])
	{
		DirTree frame = new DirTree();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}

class IconCellRenderer extends DefaultTreeCellRenderer
{
	public IconCellRenderer()
	{
		setLeafIcon(null);
		setOpenIcon(null);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		// Invoke default implementation
		Component result = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object obj = node.getUserObject();
		setText(obj.toString());
		if (obj instanceof Boolean)
			setText("Retrieving data...");
		if (obj instanceof IconData)
		{
			IconData idata = (IconData) obj;
			if (expanded)
				setIcon(idata.getExpandedIcon());
			else
				setIcon(idata.getIcon());
		}
		else
			setIcon(null);
		return result;
	}
}

class IconData
{
	protected Icon m_icon;
	protected Icon m_expandedIcon;
	protected Object m_data;

	public IconData(Icon icon, Object data)
	{
		m_icon = icon;
		m_expandedIcon = null;
		m_data = data;
	}

	public IconData(Icon icon, Icon expandedIcon, Object data)
	{
		m_icon = icon;
		m_expandedIcon = expandedIcon;
		m_data = data;
	}

	public Icon getIcon()
	{
		return m_icon;
	}

	public Icon getExpandedIcon()
	{
		return m_expandedIcon != null ? m_expandedIcon : m_icon;
	}

	public Object getObject()
	{
		return m_data;
	}

	@Override
	public String toString()
	{
		return m_data.toString();
	}
}

class FileNode
{
	protected File m_file;

	public FileNode(File file)
	{
		m_file = file;
	}

	public File getFile()
	{
		return m_file;
	}

	@Override
	public String toString()
	{
		return m_file.getName().length() > 0 ? m_file.getName() : m_file.getPath();
	}

	// Alternatively we copud sub-class TreeNode
	public boolean expand(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parent.getFirstChild();
		if (flag == null) // No flag
			return false;
		Object obj = flag.getUserObject();
		if (!(obj instanceof Boolean))
			return false; // Already expanded
		parent.removeAllChildren(); // Remove Flag
		File[] files = listFiles();
		if (files == null)
			return true;
		Vector v = new Vector();
		for (int k = 0; k < files.length; k++)
		{
			File f = files[k];
			if (!(f.isDirectory()))
				continue;
			FileNode newNode = new FileNode(f);
			boolean isAdded = false;
			for (int i = 0; i < v.size(); i++)
			{
				FileNode nd = (FileNode) v.elementAt(i);
				if (newNode.compareTo(nd) < 0)
				{
					v.insertElementAt(newNode, i);
					isAdded = true;
					break;
				}
			}
			if (!isAdded)
				v.addElement(newNode);
		}
		for (int i = 0; i < v.size(); i++)
		{
			FileNode nd = (FileNode) v.elementAt(i);
			IconData idata = new IconData(DirTree.ICON_FOLDER, DirTree.ICON_EXPANDEDFOLDER, nd);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(idata);
			parent.add(node);
			if (nd.hasSubDirs())
				node.add(new DefaultMutableTreeNode(new Boolean(true)));
		}
		return true;
	}

	public boolean hasSubDirs()
	{
		File[] files = listFiles();
		if (files == null)
			return false;
		for (int k = 0; k < files.length; k++)
		{
			if (files[k].isDirectory())
				return true;
		}
		return false;
	}

	public int compareTo(FileNode toCompare)
	{
		return m_file.getName().compareToIgnoreCase(toCompare.m_file.getName());
	}

	protected File[] listFiles()
	{
		if (!m_file.isDirectory())
			return null;
		try
		{
			return m_file.listFiles();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Error reading directory " + m_file.getAbsolutePath(), DirTree.APP_NAME,
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
}