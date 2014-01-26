package xoric.prism.creator.custom.control;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.custom.model.SpriteCollectionModel;
import xoric.prism.creator.custom.view.ISpriteCollectionView;
import xoric.prism.data.types.IPath_r;

public class SpriteCollectionControl implements ISpriteCollectionControl
{
	private SpriteCollectionModel model;
	private ISpriteCollectionView view;

	public SpriteCollectionControl(ISpriteCollectionView view)
	{
		this.view = view;
	}

	@Override
	public void requestCreateNewObject(INewDialogResult result)
	{
		model = new SpriteCollectionModel(result.getPath());
		model.load();

		view.setModel(model);
		view.displaySprites();
	}

	@Override
	public void requestOpenObject(IPath_r path)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void requestCloseObject()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void requestExit()
	{
		System.exit(0);
	}
}