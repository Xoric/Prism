package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.edit.UIEdit;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.PrismTextDialog;

class TitleWrapper extends Wrapper
{
	private final UIEdit edit;

	public TitleWrapper(UIEdit c, DefaultMutableTreeNode node)
	{
		super(node, "title");
		edit = c;
	}

	@Override
	public void showDialog()
	{
		Text t = PrismTextDialog.getInstance().showDialog("Input title", "Enter title", edit.getTitle().toString());

		if (t != null)
			edit.setTitle(t);
	}

	@Override
	protected String getValue()
	{
		return edit.getTitle() == null ? "null" : edit.getTitle().toString();
	}
}
