package xoric.prism.creator.custom.control;

import xoric.prism.creator.common.spritelist.view.HotspotList;
import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.custom.generators.collection.CollectionGenerator;
import xoric.prism.creator.custom.generators.texture.TextureGenerator;
import xoric.prism.creator.custom.model.CollectionModel;
import xoric.prism.creator.custom.view.ISpriteCollectionView;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IText_r;

public class MainControl implements IMainControl
{
	private CollectionModel model;
	private ISpriteCollectionView view;

	public MainControl(ISpriteCollectionView view)
	{
		this.view = view;
	}

	/* -------------- IMainControl ------------ */

	@Override
	public void requestCreateTexture()
	{
		TextureGenerator.startCreation(model);
	}

	@Override
	public void requestCreateCollection()
	{
		CollectionGenerator g = new CollectionGenerator(model);
		g.generateModel();
	}

	@Override
	public void requestSetName(IText_r name)
	{
		if (model != null)
		{
			model.setName(name);
			view.displayName();
			CollectionControl.saveModel(model);
		}
	}

	/* -------------- IMainMenuListener ------------ */

	@Override
	public void requestCreateNewProject(INewDialogResult result)
	{
		model = CollectionControl.createNewModel(result);

		if (model != null)
		{
			view.setModel(model);
			view.displayAll();
			view.updatedHotspots();
		}
	}

	@Override
	public void requestOpenProject(IPath_r path)
	{
		model = CollectionControl.openModel(path);

		if (model != null)
		{
			view.setModel(model);
			view.displayAll();
			view.updatedHotspots();
		}
	}

	@Override
	public void requestCloseProject()
	{
		model = null;
		view.setModel(null);
		view.displayAll();
	}

	@Override
	public void requestExit()
	{
		System.exit(0);
	}

	/* -------------- IObjectControl ------------ */

	@Override
	public void requestAddObject()
	{
		if (model != null)
		{
			ObjectControl.addObject(model);
			CollectionControl.saveModel(model);

			view.displayObjects();
		}
	}

	@Override
	public void requestDeleteObject(int index)
	{
		if (model != null)
		{
			ObjectControl.deleteObject(model, index);
			CollectionControl.saveModel(model);

			view.displayObjects();
			view.displayObject();
		}
	}

	@Override
	public void requestMoveObject(int index, boolean moveUp)
	{
		if (model != null)
		{
			ObjectControl.moveObject(model, index, moveUp);
			CollectionControl.saveModel(model);

			view.displayObjects();
			view.displayObject();

			view.selectObject(index + (moveUp ? -1 : 1));
		}
	}

	/* -------------- IRectControl ------------ */

	@Override
	public void requestSaveCollection()
	{
		if (model != null)
			CollectionControl.saveModel(model);
	}

	@Override
	public void setHotspotList(int spriteIndex, HotspotList list)
	{
		int objectIndex = view.getSelectedObjectIndex();
		model.setHotspotList(objectIndex, spriteIndex, list);
		view.updatedHotspots();
		requestSaveCollection();
	}

	@Override
	public HotspotList getHotspotList(int spriteIndex) throws PrismException
	{
		int objectIndex = view.getSelectedObjectIndex();
		return model.getHotspotList(objectIndex, spriteIndex);
	}

	@Override
	public void requestEnableHotspots(boolean b)
	{
		model.setHotspotListEnabled(b);
		view.updatedHotspots();
		requestSaveCollection();
	}
}
