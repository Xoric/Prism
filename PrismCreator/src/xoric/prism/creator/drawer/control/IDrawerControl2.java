package xoric.prism.creator.drawer.control;

import xoric.prism.creator.drawer.view.NewModelData;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.world.entities.AnimationIndex;

public interface IDrawerControl2
{
	public void requestNewModel(NewModelData data);

	public void requestOpenModel();

	public void requestSaveModel();

	public void requestSetName(IText_r name);

	public void requestSetTileSize(IPoint_r tileSize);

	public void requestAddAnimation(AnimationIndex animation);

	public void requestDeleteAnimation(AnimationIndex animation);

	public boolean askSaveChanges();
}
