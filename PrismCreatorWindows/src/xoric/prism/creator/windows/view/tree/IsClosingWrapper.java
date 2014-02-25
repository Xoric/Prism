package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.ui.button.UIButton;

class IsClosingWrapper extends Wrapper
{
	private final UIButton button;

	public IsClosingWrapper(UIButton c, DefaultMutableTreeNode node)
	{
		super(node, "closes window");
		button = c;
	}

	@Override
	public void showDialog()
	{
		button.setClosingWindow(!button.isClosingWindow());
	}

	@Override
	protected String getValue()
	{
		return button.isClosingWindow() ? "true" : "false";
	}
}
