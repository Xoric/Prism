package xoric.prism.creator.custom.view;

import xoric.prism.creator.custom.control.IMainControl;
import xoric.prism.creator.custom.model.SpriteCollectionModel;

public interface ISpriteCollectionView
{
	public void setControl(IMainControl control);

	public void setModel(SpriteCollectionModel model);

	public void displayAll();

	public void displayName();

	public void displayObjects();

	public void displayObject();
}
