package xoric.prism.creator.drawer.image;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.data.types.IPoint_r;

public interface IAnimationView extends IAnimationPanel
{
	public void displayAnimation(AnimationModel m); // TODO: needed?

	public void setControl(IDrawerControl control);// TODO: needed?

	public void setTileSize(IPoint_r tileSize);// TODO: needed?

	public void reloadCurrentAnimationFrames();
}
