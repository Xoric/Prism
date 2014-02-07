package xoric.prism.creator.custom.view;

import xoric.prism.creator.custom.control.IMainControl;
import xoric.prism.creator.custom.model.CollectionModel;

public interface ISpriteCollectionView
{
	public void setControl(IMainControl control);

	public void setModel(CollectionModel model);

	public void displayAll();

	public void displayName();

	public void displayObjects();

	public void displayObject();

	public void selectObject(int index);
}
