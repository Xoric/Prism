package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.IUITextComponent;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.PrismTextDialog;

class TextWrapper extends Wrapper
{
	private final IUITextComponent component;

	public TextWrapper(IUITextComponent c, DefaultMutableTreeNode node)
	{
		super(node, "text");
		component = c;
	}

	@Override
	public void showDialog()
	{
		Text t = PrismTextDialog.getInstance().showDialog("Input text", "Enter text", component.getText().toString());

		if (t != null)
			component.setText(t);
	}

	@Override
	protected String getValue()
	{
		return component.getText() == null ? "null" : component.getText().toString();
	}
}
