package xoric.prism.creator.drawer.control;

import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.view.IDrawerView2;
import xoric.prism.creator.drawer.view.NewModelData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Path;
import xoric.prism.swing.input.PathInput;
import xoric.prism.world.entities.AnimationIndex;

public class DrawerControl2 implements IDrawerControl2
{
	private IDrawerView2 view;
	private DrawerModel model;

	public DrawerControl2(IDrawerView2 view)
	{
		this.view = view;
		this.model = new DrawerModel();
	}

	/* *********** IDrawerControl2 ****************** */

	@Override
	public void requestNewModel(NewModelData data)
	{
		boolean isOK = askSaveChanges();
		if (!isOK)
			return;

		try
		{
			DrawerModel newModel = new DrawerModel(data);
			newModel.preparePath();

			model = newModel;
			view.displayAll(newModel);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	@Override
	public void requestOpenModel()
	{
		DrawerModel openedModel = null;
		Path path = PathInput.showDialog("Open model");

		if (path != null)
		{
			openedModel = new DrawerModel();
			try
			{
				openedModel.load(path);
			}
			catch (IOException e)
			{
				openedModel = null;
				JOptionPane.showMessageDialog(null, "An error occured while trying to load a model from the specified directory:\n\n"
						+ e.getMessage(), "Open model", JOptionPane.WARNING_MESSAGE);
			}
		}

		if (openedModel != null)
		{
			model = openedModel;
			view.displayAll(openedModel);
		}
	}

	@Override
	public void requestSaveModel()
	{
		if (model.hasChanges())
			saveChanges();
	}

	@Override
	public void requestSetName(IText_r name)
	{
		if (!model.getName().equals(name))
		{
			model.setName(name);
			view.displayName(name);

			updateSaveState();
		}
	}

	@Override
	public void requestSetTileSize(IPoint_r tileSize)
	{
		model.setTileSize(tileSize);
		view.displayTileSize(tileSize);
	}

	@Override
	public void requestAddAnimation(AnimationIndex animation)
	{
		System.out.println("requestAddAnimation(" + animation + ")");

		//		view.displayAnimation(animation, true);
	}

	@Override
	public void requestDeleteAnimation(AnimationIndex animation)
	{
		System.out.println("requestDeleteAnimation(" + animation + ")");

		//		view.displayAnimation(animation, false);
	}

	@Override
	public boolean askSaveChanges()
	{
		boolean isOK = !model.hasChanges();

		if (!isOK)
		{
			int result = JOptionPane.showConfirmDialog(null,
					"The current model has unsaved changes. Would you like to save before closing?", "Save changes",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == 0) // 0: Yes, save changes
			{
				isOK = saveChanges();
			}
			else
			{
				isOK = result == 1; // 1: No, discard | 2: Cancel
			}
		}
		return isOK;
	}

	/* ***************** internal ****************** */

	private void updateSaveState()
	{
		boolean canSave = model != null && model.hasChanges();
		view.displaySaveState(canSave);
	}

	private boolean saveChanges()
	{
		boolean isOK;
		try
		{
			model.save();
			isOK = true;
		}
		catch (PrismException e)
		{
			e.user.showMessage();
			isOK = false;
		}
		return isOK;
	}
}
