package xoric.prism.creator.models.image;

import xoric.prism.creator.models.control.IMainControl;
import xoric.prism.creator.models.model.VariationList;
import xoric.prism.data.types.IPoint_r;

public interface IAnimationView extends IAnimationPanel
{
	public void displayAnimation(VariationList list);

	public void setControl(IMainControl control);

	public void setTileSize(IPoint_r tileSize);

	public void reloadCurrentAnimationFrames();
}
