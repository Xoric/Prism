package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.button.ButtonActionIndex;
import xoric.prism.client.ui.button.UIButton;

class ActionIndexWrapper extends Wrapper
{
	private final UIButton button;

	public ActionIndexWrapper(UIButton c, DefaultMutableTreeNode node)
	{
		super(node, "action index");
		button = c;
	}

	@Override
	public void showDialog()
	{
		ActionIndexDialog d = new ActionIndexDialog(button.getActionIndex());
		boolean b = d.showDialog();

		if (b)
		{
			ButtonActionIndex a = d.getResult();
			button.setActionIndex(a);
		}
	}

	@Override
	protected String getValue()
	{
		ButtonActionIndex a = button.getActionIndex();
		return a == null ? "null" : a.toString();
	}
}
