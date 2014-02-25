package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.ClientActionCommand;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.ui.button.UIButton;

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
		ActionIndexDialog d = new ActionIndexDialog(button.getActionCommand());
		boolean b = d.showDialog();

		if (b)
		{
			ClientActionCommand a = d.getResult();
			button.setActionCommand(new Text(a.toString()));
		}
	}

	@Override
	protected String getValue()
	{
		IText_r t = button.getActionCommand();
		return t == null ? "null" : t.toString();
	}
}
