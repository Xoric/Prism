package xoric.prism.creator.models.image;

import xoric.prism.creator.models.control.IDrawerControl;
import xoric.prism.creator.models.model.VariationList;
import xoric.prism.data.types.IPoint_r;

public interface IAnimationView extends IAnimationPanel
{
	public void displayAnimation(VariationList list);

	public void setControl(IDrawerControl control);

	public void setTileSize(IPoint_r tileSize);

	public void reloadCurrentAnimationFrames();
}
