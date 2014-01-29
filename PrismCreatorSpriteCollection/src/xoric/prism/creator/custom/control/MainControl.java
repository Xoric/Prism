package xoric.prism.creator.custom.control;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.custom.generators.collection.CollectionGenerator;
import xoric.prism.creator.custom.generators.texture.TextureGenerator;
import xoric.prism.creator.custom.model.SpriteCollectionModel;
import xoric.prism.creator.custom.view.ISpriteCollectionView;
import xoric.prism.data.types.IPath_r;

public class MainControl implements IMainControl
{
	private SpriteCollectionModel model;
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

	/* -------------- IMainMenuListener ------------ */

	@Override
	public void requestCreateNewProject(INewDialogResult result)
	{
		model = CollectionControl.createNewModel(result);

		if (model != null)
		{
			view.setModel(model);
			view.displayAll();
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

	/* -------------- IRectControl ------------ */

	@Override
	public void requestSaveCollection()
	{
		if (model != null)
			CollectionControl.saveModel(model);
	}
}
