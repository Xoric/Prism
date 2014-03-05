package xoric.prism.creator.custom.control;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.custom.model.CollectionModel;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.PrismTextDialog;

public abstract class ObjectControl
{
	public static void addObject(CollectionModel model)
	{
		ObjectModel m = null;
		Text name = PrismTextDialog.getInstance().showDialog("New Object", "Object name (optional)", "NEW OBJECT");

		if (name != null)
		{
			m = new ObjectModel(name);
			model.addObjectModel(m);

			try
			{
				model.save();
			}
			catch (PrismException e)
			{
				e.code.print();
				e.user.showMessage();
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "There was an I/O error while trying to save changes: " + e.toString(), "Add Object",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public static void deleteObject(CollectionModel model, int index)
	{
		if (model != null)
		{
			ObjectModel m = model.getObjectModel(index);
			IText_r name = m.getName();

			int res = JOptionPane.showConfirmDialog(null, "Please confirm deleting the object " + name + ".", "Delete object",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (res == JOptionPane.YES_OPTION)
			{
				SpriteNameGenerator s = new CollectionSpriteNameGenerator(model.getPath(), name);

				int i = 0;
				boolean b;
				do
				{
					File f = s.getFile(i);
					b = f.exists();
					if (b)
					{
						f.delete();
						++i;
					}
				}
				while (b);

				model.deleteObject(index);
			}
		}
	}

	public static void moveObject(CollectionModel model, int index, boolean moveUp)
	{
		model.moveObjectModel(index, moveUp);
	}
}
