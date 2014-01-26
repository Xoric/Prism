package xoric.prism.creator.custom.view;

import xoric.prism.creator.custom.control.ISpriteCollectionControl;
import xoric.prism.creator.custom.model.SpriteCollectionModel;

public interface ISpriteCollectionView
{
	public void setControl(ISpriteCollectionControl control);

	public void setModel(SpriteCollectionModel model);

	public void displaySprites();
}