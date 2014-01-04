package xoric.prism.creator.drawer.control;

import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.view.IDrawerView2;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Path;
import xoric.prism.swing.input.PathInput;

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
	public void requestNewModel()
	{
		boolean isOK = askSaveChanges();
		if (!isOK)
			return;

		boolean tryAgain = false;
		DrawerModel newModel = null;
		do
		{
			Path path = PathInput.showDialog("Choose a working directory");

			if (path != null)
			{
				newModel = new DrawerModel();
				try
				{
					newModel.initPath(path);
				}
				catch (IOException e)
				{
					newModel = null;
					String[] options = new String[] { "Choose another", "Cancel" };
					int res = JOptionPane.showOptionDialog(null, "An error occured while trying to write to the specified directory:\n\n",
							"New model", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					tryAgain = res == 0;
				}
			}
		}
		while (tryAgain);

		if (newModel != null)
		{
			model = newModel;
			view.displayAll(newModel);
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

	/* ***************** internal ****************** */

	private void updateSaveState()
	{
		boolean canSave = model != null && model.hasChanges();
		view.displaySaveState(canSave);
	}

	private boolean askSaveChanges()
	{
		boolean isOK = !model.hasChanges();

		if (!isOK)
		{
			int result = JOptionPane.showConfirmDialog(null,
					"The current model contains unsaved changes. Would you like to save before closing?", "Save changes",
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

	private boolean saveChanges()
	{
		boolean isOK;
		try
		{
			model.save();
			isOK = true;
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "An error occured while saving." + e.getMessage(), "Save model",
					JOptionPane.WARNING_MESSAGE);
			isOK = false;
		}
		return isOK;
	}
}