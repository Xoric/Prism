package xoric.prism.creator.drawer.image;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.VariationList;
import xoric.prism.data.types.IPoint_r;

public interface IAnimationView extends IAnimationPanel
{
	public void displayAnimation(VariationList list);

	public void setControl(IDrawerControl control);

	public void setTileSize(IPoint_r tileSize);

	public void reloadCurrentAnimationFrames();
}
