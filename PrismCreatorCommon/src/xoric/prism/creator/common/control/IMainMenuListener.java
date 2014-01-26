package xoric.prism.creator.common.control;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.data.types.IPath_r;

public interface IMainMenuListener
{
	public void requestCreateNewObject(INewDialogResult result);

	public void requestOpenObject(IPath_r path);

	public void requestCloseObject();

	public void requestExit();
}
