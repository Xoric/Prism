package xoric.prism.creator.windows.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import xoric.prism.ui.Ruler;
import xoric.prism.ui.UIComponentH;

class HeightWrapper extends Wrapper
{
	private final UIComponentH component;

	public HeightWrapper(UIComponentH c, DefaultMutableTreeNode node)
	{
		super(node, "height");
		component = c;
	}

	@Override
	public void showDialog()
	{
		Ruler r = PrismRulerDialog.getInstance().showDialog("Input height", "Enter height", component.getHeightRuler());

		if (r != null)
			component.setHeightRuler(r.constant, r.factor);
	}

	@Override
	protected String getValue()
	{
		return component.getHeightRuler().toString();
	}
}
