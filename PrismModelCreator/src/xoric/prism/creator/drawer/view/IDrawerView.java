package xoric.prism.creator.drawer.view;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.types.IPath_r;

public interface IDrawerView extends IModelTable, IPortraitView, IAnimationList
{
	public void setModel(DrawerModel model);

	public void displayAll(DrawerModel model);

	public void displayPath(IPath_r path);

	public void updateCurrentAnimation();

	public void setHourglass(boolean b);
}
