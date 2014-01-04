package xoric.prism.creator.drawer.control;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;

public interface IDrawerControl2
{
	public void requestNewModel();

	public void requestOpenModel();

	public void requestSaveModel();

	public void requestSetName(IText_r name);

	public void requestSetTileSize(IPoint_r tileSize);
}