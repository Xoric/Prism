package xoric.prism.creator.windows.view.tree;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.ui.button.ButtonActionMask;
import xoric.prism.ui.button.UIButton;

class ActionMaskWrapper extends Wrapper
{
	private final UIButton button;

	public ActionMaskWrapper(UIButton c, DefaultMutableTreeNode node)
	{
		super(node, "action mask");
		button = c;
	}

	@Override
	public void showDialog()
	{
		ButtonActionMask m = button.getActionMask();
		ActionMaskDialog d = new ActionMaskDialog(m);
		boolean b = d.showDialog();

		if (b)
		{
			List<Integer> l1 = d.getIntSources();
			List<Integer> l2 = d.getTextSources();

			if (l1 != null && l2 != null)
			{
				m = new ButtonActionMask();
				m.setIntSources(l1);
				m.setTextSources(l2);
				button.setActionMask(m);
			}
		}
	}

	@Override
	protected String getValue()
	{
		ButtonActionMask m = button.getActionMask();
		return m == null ? "null" : m.toString();
	}
}
