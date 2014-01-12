package xoric.prism.creator.drawer.control;

import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.view.NewModelData;
import xoric.prism.creator.drawer.view.NewModelDialog;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Path;
import xoric.prism.swing.input.OpenPathDialog;

public class ModelControl extends ControlLayer
{
	public ModelControl(DrawerModel model, IBusyControl busyControl)
	{
		super(model, busyControl);
	}

	private boolean askSaveChanges()
	{
		boolean isOK = model == null || !model.hasChanges();

		if (!isOK)
		{
			int result = JOptionPane.showConfirmDialog(null,
					"The current model has unsaved changes. Would you like to save before closing?", "Save changes",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == 0) // 0: Yes, save changes
			{
				saveModel();
			}
			else
			{
				isOK = result == 1; // 1: No, discard | 2: Cancel
			}
		}
		return isOK;
	}

	private boolean saveModel()
	{
		boolean isOK = !model.hasChanges();

		if (!isOK)
		{
			busyControl.setBusy(true);
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
			busyControl.setBusy(false);
		}
		return isOK;
	}

	public DrawerModel createNewModel()
	{
		DrawerModel newModel = null;

		if (askSaveChanges())
		{
			NewModelDialog d = new NewModelDialog();
			boolean isOK = d.show();

			if (isOK)
			{
				busyControl.setBusy(true);

				try
				{
					NewModelData data = d.getResult();
					newModel = new DrawerModel(data);
					newModel.preparePath();
				}
				catch (PrismException e)
				{
					newModel = null;
					e.user.showMessage();
				}

				busyControl.setBusy(false);
			}
		}
		return newModel;
	}

	public DrawerModel openModel()
	{
		DrawerModel openedModel = null;

		if (askSaveChanges())
		{
			OpenPathDialog d = new OpenPathDialog("Open Model", "Please enter the working directory of the model you want to open.");
			boolean b = d.show();
			Path path = b ? d.getResult() : null;

			if (path != null)
			{
				busyControl.setBusy(true);

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

				busyControl.setBusy(false);
			}
		}
		return openedModel;
	}

	public boolean closeModel()
	{
		return askSaveChanges();
	}

	public void setName(IText_r name)
	{
		boolean b = !model.getName().equals(name);
		if (b)
		{
			model.setName(name);
			saveModel();
		}
	}

	public void setTileSize(IPoint_r tileSize)
	{
		model.setTileSize(tileSize);
	}

	public void generateAnimations(DrawerModel model)
	{
		IPath_r path = model.getPath();
		GridGenerator g = new GridGenerator(path);
		g.generateAll();
	}

	public void exportModel(DrawerModel model)
	{
		IPath_r path = model.getPath();
		System.out.println("export model");
	}
}
