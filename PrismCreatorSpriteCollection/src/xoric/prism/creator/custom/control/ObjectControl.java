package xoric.prism.creator.custom.control;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.custom.SpriteCollectionSpriteNameGenerator;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.creator.custom.model.SpriteCollectionModel;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.PrismTextDialog;

public abstract class ObjectControl
{
	public static void addObject(SpriteCollectionModel model)
	{
		ObjectModel m = null;
		Text name = PrismTextDialog.getInstance().showDialog("New Object", "Object name (optional)", "NEW OBJECT");

		if (name != null)
		{
			m = new ObjectModel(name);
			model.addObject(m);

			try
			{
				model.save();
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "There was an I/O error while trying to save changes: " + e.toString(), "Add Object",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public static void deleteObject(SpriteCollectionModel model, int index)
	{
		if (model != null)
		{
			ObjectModel m = model.getObjectModel(index);
			IText_r name = m.getName();

			int res = JOptionPane.showConfirmDialog(null, "Please confirm deleting the object " + name + ".", "Delete object",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (res == JOptionPane.YES_OPTION)
			{
				SpriteNameGenerator s = new SpriteCollectionSpriteNameGenerator(model.getPath(), name);

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
}