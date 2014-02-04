package xoric.prism.client.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

public class UiFrame extends FloatRect implements IDrawableUI
{
	private Text title;

	public void setTitle(IText_r title)
	{
		this.title = new Text(title);
	}

	public void setTitle(Text title)
	{
		this.title = title;
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		Materials.framesDrawer.setup(0, 1, 0);
		Materials.framesDrawer.drawNineParts(this);

		if (title != null)
		{
			Materials.framesDrawer.setup(2, 0);
			Materials.framesDrawer.drawThreeParts(topLeft, size.x);

			Materials.printer.print(topLeft, title);
		}
	}
}
