package xoric.prism.creator.drawer.image;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.VariationList;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.entities.ViewAngle;

public interface ISpriteList
{
	public void loadFrames(VariationList list, int variation, ViewAngle v);

	public void setControl(IDrawerControl control);

	public void setSpriteSize(IPoint_r tileSize);
}
