package xoric.prism.creator.drawer.control;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class SpriteControl extends ControlLayer
{
	public SpriteControl(DrawerModel model, IBusyControl busyControl)
	{
		super(model, busyControl);
	}

	public void addSprite(AnimationIndex a, ViewAngle v, int index)
	{
		IPath_r path = model.getPath();

		// find highest existing index
		int n = index;
		boolean b;
		do
		{
			String filename = AnimationModel.getFileName(a, v, n);
			File file = path.getFile(filename);
			b = file.exists();
			if (b)
				++n;
		}
		while (b);

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
				f1 = path.getFile(AnimationModel.getFileName(a, v, i));
				f2 = path.getFile(AnimationModel.getFileName(a, v, i + 1));
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
			file = path.getFile(AnimationModel.getFileName(a, v, index));
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
}
