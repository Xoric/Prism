package xoric.prism.creator.custom.control;

import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.custom.model.CollectionModel;
import xoric.prism.creator.custom.view.NewCollectionData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Text;

public abstract class CollectionControl
{
	public static CollectionModel createNewModel(INewDialogResult result)
	{
		NewCollectionData d = (NewCollectionData) result;
		CollectionModel m = openModel(result.getPath());
		m.setName(d.getName());
		return m;
	}

	public static CollectionModel openModel(IPath_r path)
	{
		CollectionModel model = new CollectionModel(new Text("TEST"), path);
		try
		{
			model.load();
		}
		catch (IOException e)
		{
			model = null;
			JOptionPane.showConfirmDialog(null, "There was an I/O error: " + e.toString());
		}
		catch (PrismException e)
		{
			model = null;
			e.code.print();
			e.user.showMessage();
		}
		return model;
	}

	public static void saveModel(CollectionModel model)
	{
		try
		{
			model.save();
		}
		catch (Exception e)
		{
			JOptionPane.showConfirmDialog(null, "There was an I/O error while trying to save: " + e.toString());
		}
	}
}
