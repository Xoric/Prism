package xoric.prism.creator.common.control;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.data.types.IPath_r;

public interface IMainMenuListener
{
	public void requestCreateNewProject(INewDialogResult result);

	public void requestOpenProject(IPath_r path);

	public void requestCloseProject();

	public void requestExit();
}
