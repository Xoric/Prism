package xoric.prism.creator.drawer.control;

import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.types.Path;
import xoric.prism.swing.input.PathInput;

/**
 * Processes user inputs and adapts {@link DrawerModel} accordingly.
 * @author XoricLee
 */
public class DrawerControl
{
	private DrawerModel model;

	public void showNewModelDialog()
	{
		boolean tryAgain = false;
		DrawerModel model = null;

		do
		{
			Path path = PathInput.showDialog("Choose a working directory");

			if (path != null)
			{
				model = new DrawerModel();
				try
				{
					model.initPath(path);
				}
				catch (IOException e)
				{
					model = null;
					String[] options = new String[] { "Choose another", "Cancel" };
					int res = JOptionPane.showOptionDialog(null, "An error occured while trying to write to the specified directory:\n\n",
							"New model", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					tryAgain = res == 0;
				}
			}
		}
		while (tryAgain);

		if (model != null)
		{
			view.setModel(model);
		}
	}

	public DrawerModel showOpenModelDialog()
	{
		DrawerModel model = null;
		Path path = PathInput.showDialog("Open model");

		if (path != null)
		{
			model = new DrawerModel();
			try
			{
				model.load(path);
			}
			catch (IOException e)
			{
				model = null;
				JOptionPane.showMessageDialog(null, "An error occured while trying to load a model from the specified directory:\n\n"
						+ e.getMessage(), "Open model", JOptionPane.WARNING_MESSAGE);
			}
		}
		return model;
	}

	public boolean saveChanges(DrawerModel model)
	{
		boolean isOK;
		try
		{
			model.save();
			isOK = true;
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "An error occured while trying to save." + e.getMessage(), "Save model",
					JOptionPane.WARNING_MESSAGE);
			isOK = false;
		}
		return isOK;
	}

	public boolean askSaveChanges(DrawerModel model)
	{
		boolean isOK = model == null || !model.hasChanges();

		if (!isOK)
		{
			int result = JOptionPane.showConfirmDialog(null,
					"The current model contains unsaved changes. Would you like to save before closing?", "Save changes",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == 0) // 0: Yes, save changes
			{
				isOK = saveChanges(model);
			}
			else
			{
				isOK = result == 1; // 1: No, discard | 2: Cancel
			}
		}
		return isOK;
	}
}
