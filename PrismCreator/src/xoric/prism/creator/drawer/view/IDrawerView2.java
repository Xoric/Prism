package xoric.prism.creator.drawer.view;

import xoric.prism.creator.drawer.image.IAnimationView;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.types.IPath_r;

public interface IDrawerView2 extends IModelTable, IAnimationList, IAnimationView
{
	public void displayAll(DrawerModel model);

	public void displayPath(IPath_r path);

	public void displaySaveState(boolean canSave);

	public void displayAnimations(AnimationModel[] animations);
}
