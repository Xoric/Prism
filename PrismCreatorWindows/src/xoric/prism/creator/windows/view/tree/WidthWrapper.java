package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.Ruler;
import xoric.prism.client.ui.UIComponent;

class WidthWrapper extends Wrapper
{
	private final UIComponent component;

	public WidthWrapper(UIComponent c, DefaultMutableTreeNode node)
	{
		super(node, "width");
		component = c;
	}

	@Override
	public void showDialog()
	{
		Ruler r = PrismRulerDialog.getInstance().showDialog("Input width", "Enter width", component.getWidthRuler());

		if (r != null)
			component.setWidthRuler(r.constant, r.factor);
	}

	@Override
	protected String getValue()
	{
		return component.getWidthRuler().toString();
	}
}
