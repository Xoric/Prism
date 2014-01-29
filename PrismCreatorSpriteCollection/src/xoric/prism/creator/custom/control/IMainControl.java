package xoric.prism.creator.custom.control;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.creator.custom.view.IRectControl;

public interface IMainControl extends IMainMenuListener, IObjectControl, IRectControl
{
	public void requestCreateTexture();

	public void requestCreateCollection();
}
