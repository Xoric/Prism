package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.UIWindow;

class ResizableWrapper extends Wrapper
{
	private final UIWindow window;

	public ResizableWrapper(UIWindow w, DefaultMutableTreeNode node)
	{
		super(node, "resizable");
		window = w;
	}

	@Override
	public void showDialog()
	{
		window.makeResizable(!window.isResizable());
	}

	@Override
	protected String getValue()
	{
		return window.isResizable() ? "true" : "false";
	}
}
