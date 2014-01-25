package xoric.prism.creator.models.control;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;

public interface IModelControl extends IMainMenuListener
{
	public void requestSetName(IText_r name);

	public void requestResizeSprites(IPoint_r tileSize);

	public void requestCreateAnimations();

	public void requestCreateModel();

	public void requestCreateNewPortrait();

	public void requestImportPortrait();

	public void requestEditPortrait();

	public void requestDeletePortrait();
}
