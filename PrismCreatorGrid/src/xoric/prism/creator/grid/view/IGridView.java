package xoric.prism.creator.grid.view;

import xoric.prism.creator.grid.control.IGridControl;
import xoric.prism.creator.grid.model.GridModel;

public interface IGridView
{
	public void setControl(IGridControl control);

	public void setModel(GridModel model);

	public void displayAll();

	public void displayName();

	public void displaySpriteSize();

	public void displaySprites();

	public void updateHotspotsEnabled();

	public void updatedHotSpots();
}
