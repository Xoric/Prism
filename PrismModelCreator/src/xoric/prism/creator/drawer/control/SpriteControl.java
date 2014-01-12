package xoric.prism.creator.drawer.control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.model.SpriteNames;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class SpriteControl extends ControlLayer
{
	private final ExternalImageEditor externalEditor;

	public SpriteControl(DrawerModel model, IBusyControl busyControl)
	{
		super(model, busyControl);

		externalEditor = new ExternalImageEditor();
	}

	private int findHighestIndex(IPath_r path, AnimationIndex a, ViewAngle v)
	{
		int n = 0;
		boolean b;
		do
		{
			String filename = SpriteNames.getFilename(a, v, n);
			File file = path.getFile(filename);
			b = file.exists();
			if (b)
				++n;
		}
		while (b);
		return n - 1;
	}

	public void addSprite(AnimationIndex a, ViewAngle v, int index)
	{
		IPath_r path = model.getPath();

		// find highest existing index
		int n = findHighestIndex(path, a, v);

		// increase all indices equal or greater than the given parameter by one
		try
		{
			renameFiles(path, a, v, n, index);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
			return;
		}

		// create new sprite
		try
		{
			createSprite(path, a, v, index);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	private void renameFiles(IPath_r path, AnimationIndex a, ViewAngle v, int highest, int lowest) throws PrismException
	{
		File f1 = null;
		File f2 = null;
		try
		{
			for (int i = highest; i >= lowest; --i)
			{
				f1 = path.getFile(SpriteNames.getFilename(a, v, i));
				f2 = path.getFile(SpriteNames.getFilename(a, v, i + 1));
				f1.renameTo(f2);
			}
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was an error while trying to rename a file.");

			if (f1 != null && f2 != null)
			{
				e.addInfo("file", f1.toString());
				e.addInfo("new name", f2.getName());
			}
			throw e;
		}
	}

	private void createSprite(IPath_r path, AnimationIndex a, ViewAngle v, int index) throws PrismException
	{
		File file = null;
		try
		{
			IPoint_r tileSize = model.getTileSize();
			BufferedImage img = new BufferedImage(tileSize.getX(), tileSize.getY(), BufferedImage.TYPE_INT_ARGB);
			file = path.getFile(SpriteNames.getFilename(a, v, index));
			ImageIO.write(img, "png", file);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was an error while trying to write a file.");

			if (file != null)
				e.addInfo("file", file.getName());

			throw e;
		}
	}

	public void deleteSprites(AnimationIndex a, ViewAngle v, List<Integer> indices)
	{
		int r = JOptionPane.showConfirmDialog(null, "Please confirm deleting the selected sprite(s).", "Delete sprites",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (r != JOptionPane.YES_OPTION)
			return;

		IPath_r path = model.getPath();
		File file = null;
		int i = 0;
		int gap = 0;
		boolean b;

		do
		{
			String filename = SpriteNames.getFilename(a, v, i);
			file = path.getFile(filename);
			b = file.exists();
			if (b)
			{
				if (indices.contains(i))
				{
					// delete this sprite
					try
					{
						file.delete();
					}
					catch (Exception e0)
					{
						PrismException e = new PrismException(e0);
						e.setText("There was an error while trying to delete a file.");
						e.addInfo("file", file.getName());
						e.user.showMessage();
						return;
					}

					// keep track of the gap
					++gap;
				}
				else
				{
					// keep this sprite, check if there is a gap to close
					if (gap > 0)
					{
						File file2 = path.getFile(SpriteNames.getFilename(a, v, i - gap));

						try
						{
							file.renameTo(file2);
						}
						catch (Exception e0)
						{
							PrismException e = new PrismException(e0);
							e.setText("There was an error while trying to rename a file.");
							e.addInfo("file", file.toString());
							e.addInfo("new name", file2.getName());
							e.user.showMessage();
							return;
						}
					}
				}
			}
			++i;
		}
		while (b);
	}

	public void editSprite(File file)
	{
		File programFile = externalEditor.getFile();

		if (programFile != null)
		{
			try
			{
				String[] s = { programFile.toString(), file.toString() };
				Runtime.getRuntime().exec(s);
			}
			catch (IOException e0)
			{
				PrismException e = new PrismException(e0);
				e.setText("An error occured while trying to open an image in an external editing program specified by the user.");
				e.addInfo("image file", file.toString());
				e.addInfo("editing program", programFile.toString());
				e.code.print();
				e.user.showMessage();
			}
		}
	}

	public void inputExternalImageEditor()
	{
		externalEditor.showInput();
	}
}
