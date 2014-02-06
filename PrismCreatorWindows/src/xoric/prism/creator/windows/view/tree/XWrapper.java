package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.client.ui.Ruler;
import xoric.prism.client.ui.UIComponent;

class XWrapper extends Wrapper
{
	private final UIComponent component;

	public XWrapper(UIComponent c, DefaultMutableTreeNode node)
	{
		super(node, "X");
		component = c;
	}

	@Override
	public void showDialog()
	{
		Ruler r = PrismRulerDialog.getInstance().showDialog("Input X", "Enter X", component.getXRuler());

		if (r != null)
			component.setXRuler(r.constant, r.factor);
	}

	@Override
	protected String getValue()
	{
		return component.getXRuler().toString();
	}
}
