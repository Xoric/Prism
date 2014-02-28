package xoric.prism.creator.grid.control;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.creator.common.spritelist.view.IHotSpotListener;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;

public interface IGridControl extends IMainMenuListener, IHotSpotListener
{
	public void requestRenameGrid(IText_r name);

	public void requestSetSpriteSize(IPoint_r spriteSize);

	public void requestEnableHotSpots(boolean b);

	public void requestCreateTexture();

	public void requestCreateGrid();
}
