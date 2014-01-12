package xoric.prism.creator.drawer.control;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;

public interface IModelControl
{
	public void requestNewModel();

	public void requestOpenModel();

	public void requestCloseModel();

	public void requestSetName(IText_r name);

	public void requestSetTileSize(IPoint_r tileSize);

	public void requestGenerateAnimations();

	public void requestExportModel();
}