package xoric.prism.creator.drawer.control;

import xoric.prism.creator.common.IRecentMenuListener;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;

public interface IModelControl extends IRecentMenuListener
{
	public void requestNewModel();

	public void requestOpenModel();

	public void requestCloseModel();

	public void requestSetName(IText_r name);

	public void requestResizeSprites(IPoint_r tileSize);

	public void requestGenerateAnimations();

	public void requestExportModel();

	public void requestCreateNewPortrait();

	public void requestImportPortrait();

	public void requestEditPortrait();

	public void requestDeletePortrait();
}
