package xoric.prism.creator.models.view;

import xoric.prism.creator.models.image.IAnimationPanel;
import xoric.prism.creator.models.model.ModelModel;
import xoric.prism.data.types.IPath_r;

public interface IMainView extends IModelTable, IPortraitView, IAnimationList, IAnimationPanel
{
	public void setModel(ModelModel model);

	public void displayAll(ModelModel model);

	public void displayPath(IPath_r path);

	public void reloadCurrentAnimationFrames();

	public void setHourglass(boolean b);
}
