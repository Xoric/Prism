package xoric.prism.creator.custom.view;

import xoric.prism.creator.custom.control.IObjectControl;
import xoric.prism.creator.custom.model.SpriteCollectionModel;

public interface IObjectList
{
	public void setControl(IObjectControl control);

	public void setModel(SpriteCollectionModel model);

	public void displayObjects();

	public int getSelectedIndex();

	public void setEnabled(boolean b);
}
