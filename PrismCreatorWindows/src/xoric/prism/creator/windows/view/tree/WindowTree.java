package xoric.prism.creator.windows.view.tree;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import xoric.prism.client.ui.IUITextComponent;
import xoric.prism.client.ui.UIComponent;
import xoric.prism.client.ui.UIComponentH;
import xoric.prism.client.ui.UIWindow;
import xoric.prism.creator.windows.control.IWindowControl;
import xoric.prism.creator.windows.model.WindowModel;

public class WindowTree extends JPanel implements MouseListener, ITree
{
	private static final long serialVersionUID = 1L;

	private WindowModel model;

	private final JTree tree;
	private final DefaultMutableTreeNode root;

	private IWindowControl control;

	public WindowTree()
	{
		super(new BorderLayout());

		Wrapper.tree = this;

		root = new DefaultMutableTreeNode("Root");
		tree = new JTree(root);
		tree.addMouseListener(this);

		DefaultMutableTreeNode r = new DefaultMutableTreeNode("UIButton");
		root.add(r);
		r.add(new DefaultMutableTreeNode("Text"));

		root.add(new DefaultMutableTreeNode("UITextField"));
		root.add(new DefaultMutableTreeNode("UITextField"));

		JScrollPane scroll = new JScrollPane(tree);
		add(BorderLayout.CENTER, scroll);
	}

	public void setControl(IWindowControl control)
	{
		this.control = control;
	}

	public void setModel(WindowModel m)
	{
		model = m;
	}

	public void display()
	{
		root.removeAllChildren();

		if (model != null)
			addWindow(root, model.getWindow());

		for (int i = 0; i < tree.getRowCount(); i++)
			tree.expandRow(i);

		tree.revalidate();
		tree.repaint();
	}

	private void addWindow(DefaultMutableTreeNode t, UIWindow w)
	{
		DefaultMutableTreeNode r = new DefaultMutableTreeNode(w.getClass().getSimpleName());
		t.add(r);
		addWrappers(r, w);

		for (UIComponent c : w.getComponents())
		{
			DefaultMutableTreeNode s = new DefaultMutableTreeNode(c.getClass().getSimpleName());
			r.add(s);
			addWrappers(s, c);
		}
	}

	private void addWrappers(DefaultMutableTreeNode t, UIComponent c)
	{
		DefaultMutableTreeNode r = new DefaultMutableTreeNode();
		r.setUserObject(new XWrapper(c, r));
		t.add(r);

		r = new DefaultMutableTreeNode();
		r.setUserObject(new YWrapper(c, r));
		t.add(r);

		r = new DefaultMutableTreeNode();
		r.setUserObject(new WidthWrapper(c, r));
		t.add(r);

		if (c instanceof UIComponentH)
		{
			r = new DefaultMutableTreeNode();
			r.setUserObject(new HeightWrapper((UIComponentH) c, r));
			t.add(r);
		}

		if (c instanceof IUITextComponent)
		{
			IUITextComponent c2 = (IUITextComponent) c;
			r = new DefaultMutableTreeNode();
			r.setUserObject(new TextWrapper(c2, r));
			t.add(r);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() >= 2)
		{
			Object o = tree.getSelectionPath().getLastPathComponent();

			if (o instanceof DefaultMutableTreeNode)
			{
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) o;
				o = n.getUserObject();

				if (o instanceof Wrapper)
				{
					Wrapper w = (Wrapper) o;
					w.input();
				}
			}
		}
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
	public void mousePressed(MouseEvent arg0)
	{
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
	}

	@Override
	public void onModifiedValue(TreeNode n)
	{
		DefaultTreeModel m = (DefaultTreeModel) tree.getModel();
		m.nodeChanged(n);
		control.onWindowModified();
		//		control.requestSave();
	}
}
