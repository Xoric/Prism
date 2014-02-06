package xoric.prism.client.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

public class UIFrame extends UIComponentH implements IUITextComponent
{
	protected UITextLine titleLine;

	@Override
	public void setText(Text title)
	{
		if (titleLine == null)
			registerChild(titleLine = new UITextLine());

		titleLine.setText(title);
		//		titleLine.rearrange(rect);
	}

	@Override
	public IText_r getText()
	{
		return titleLine == null ? null : titleLine.getText();
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		Materials.framesDrawer.setup(0, 1, 0);
		Materials.framesDrawer.drawNineParts(rect);

		if (titleLine != null)
		{
			int i = isMouseDown ? 1 : 0;
			Materials.framesDrawer.setup(2, i);
			Materials.framesDrawer.drawThreeParts(rect.getTopLeft(), rect.getWidth());

			titleLine.draw(renderer);
		}
	}

	@Override
	public void mouseClick() throws PrismException
	{
	}

	@Override
	protected IActiveUI mouseDownConfirmed(IFloatPoint_r mouse)
	{
		return null;
	}

	@Override
	protected void mouseUpConfirmed()
	{
	}
}
