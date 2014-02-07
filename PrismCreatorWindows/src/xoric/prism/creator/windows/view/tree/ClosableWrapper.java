package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.UIWindow;

class ClosableWrapper extends Wrapper
{
	private final UIWindow window;

	public ClosableWrapper(UIWindow w, DefaultMutableTreeNode node)
	{
		super(node, "closable");
		window = w;
	}

	@Override
	public void showDialog()
	{
		window.makeClosable(!window.isClosable());
	}

	@Override
	protected String getValue()
	{
		return window.isClosable() ? "true" : "false";
	}
}
