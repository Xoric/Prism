package xoric.prism.creator.common.control;

import xoric.prism.data.types.IPath_r;

public interface IMainMenuListener
{
	public void requestCreateNewObject(Object data);

	public void requestOpenObject(IPath_r path);

	public void requestCloseObject();

	public void requestExit();
}
