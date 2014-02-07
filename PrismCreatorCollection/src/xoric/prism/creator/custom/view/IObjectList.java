package xoric.prism.creator.custom.view;

import xoric.prism.creator.custom.control.IObjectControl;
import xoric.prism.creator.custom.model.CollectionModel;

public interface IObjectList
{
	public void setControl(IObjectControl control);

	public void setModel(CollectionModel model);

	public void displayObjects();

	public int getSelectedIndex();

	public void setEnabled(boolean b);

	public void selectObject(int index);
}
