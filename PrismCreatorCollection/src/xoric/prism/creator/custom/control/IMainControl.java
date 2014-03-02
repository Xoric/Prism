package xoric.prism.creator.custom.control;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.creator.common.spritelist.view.IHotspotListener;
import xoric.prism.creator.custom.view.IRectControl;
import xoric.prism.data.types.IText_r;

public interface IMainControl extends IMainMenuListener, IObjectControl, IRectControl, IHotspotListener
{
	public void requestCreateTexture();

	public void requestCreateCollection();

	public void requestSetName(IText_r name);
}
