package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.ui.edit.InputFormat;
import xoric.prism.ui.edit.UIEdit;

class InputFormatWrapper extends Wrapper
{
	private final UIEdit edit;

	public InputFormatWrapper(UIEdit c, DefaultMutableTreeNode node)
	{
		super(node, "input format");
		edit = c;
	}

	@Override
	public void showDialog()
	{
		InputFormatDialog d = new InputFormatDialog(edit.getInputFormat(), edit.getMaxLength());
		boolean b = d.showDialog();

		if (b)
		{
			InputFormatResult r = d.getResult();
			edit.setInputFormat(r.format, r.maxLength);
		}
	}

	@Override
	protected String getValue()
	{
		InputFormat i = edit.getInputFormat();
		return i.toString() + ", maxLength=" + edit.getMaxLength();
	}
}
