package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.ui.Ruler;
import xoric.prism.ui.UIComponent;

class YWrapper extends Wrapper
{
	private final UIComponent component;

	public YWrapper(UIComponent c, DefaultMutableTreeNode node)
	{
		super(node, "y");
		component = c;
	}

	@Override
	public void showDialog()
	{
		Ruler r = PrismRulerDialog.getInstance().showDialog("Input y", "Enter y", component.getYRuler());

		if (r != null)
			component.setYRuler(r.constant, r.factor);
	}

	@Override
	protected String getValue()
	{
		return component.getYRuler().toString();
	}
}
