package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.UIWindow;

class MoveableWrapper extends Wrapper
{
	private final UIWindow window;

	public MoveableWrapper(UIWindow w, DefaultMutableTreeNode node)
	{
		super(node, "moveable");
		window = w;
	}

	@Override
	public void showDialog()
	{
		window.setMoveable(!window.isMoveable());
	}

	@Override
	protected String getValue()
	{
		return window.isMoveable() ? "true" : "false";
	}
}
