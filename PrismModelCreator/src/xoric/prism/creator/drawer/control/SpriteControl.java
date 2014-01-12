package xoric.prism.creator.drawer.control;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
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

	public void cloneSprite(AnimationIndex a, ViewAngle v, int index)
	{
		// load sprite image
		IPath_r path = model.getPath();
		File file = path.getFile(SpriteNames.getFilename(a, v, index));
		BufferedImage bi = null;
		try
		{
			bi = ImageIO.read(file);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem reading the image to be cloned.");
			e.addInfo("image file", file.toString());
			e.user.showMessage();
			return;
		}

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

		// insert sprite
		try
		{
			writeImage(path, a, v, index, bi);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	public void insertSprite(AnimationIndex a, ViewAngle v, int index)
	{
		// find highest existing index
		IPath_r path = model.getPath();
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

	public void insertSpriteFromClipboard(AnimationIndex a, ViewAngle v, int index)
	{
		// get image from clipboard
		Image clipboardImage = null;
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

		if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor))
		{
			try
			{
				clipboardImage = (Image) t.getTransferData(DataFlavor.imageFlavor);
				if (clipboardImage == null)
					throw new Exception("image is null");
			}
			catch (Exception e0)
			{
				PrismException e = new PrismException(e0);
				e.setText("There was a problem while trying to get an image from clipboard.");
				e.user.showMessage();
				return;
			}
		}

		// find highest existing index
		IPath_r path = model.getPath();
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

		// insert sprite
		try
		{
			BufferedImage bi = embedClipboard(clipboardImage);
			writeImage(path, a, v, index, bi);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	private BufferedImage embedClipboard(Image clipboardImage)
	{
		BufferedImage bi = new BufferedImage(clipboardImage.getWidth(null), clipboardImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(clipboardImage, 0, 0, null);
		g2d.dispose();

		int w = bi.getWidth();
		int h = bi.getHeight();
		int tc = bi.getRGB(0, 0);

		for (int y = 0; y < h; ++y)
			for (int x = 0; x < w; ++x)
				if (bi.getRGB(x, y) == tc)
					bi.setRGB(x, y, 0);

		IPoint_r spriteSize = model.getSpriteSize();
		BufferedImage img = new BufferedImage(spriteSize.getX(), spriteSize.getY(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		int x = spriteSize.getX() / 2 - w / 2;
		int y = spriteSize.getY() - h;
		g.drawImage(bi, x, y, null);
		g.dispose();

		return img;
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
		IPoint_r spriteSize = model.getSpriteSize();
		BufferedImage img = new BufferedImage(spriteSize.getX(), spriteSize.getY(), BufferedImage.TYPE_INT_ARGB);
		writeImage(path, a, v, index, img);
	}

	private void writeImage(IPath_r path, AnimationIndex a, ViewAngle v, int index, BufferedImage bi) throws PrismException
	{
		File file = path.getFile(SpriteNames.getFilename(a, v, index));
		writeImage(file, bi);
	}

	private void writeImage(File file, BufferedImage bi) throws PrismException
	{
		try
		{
			ImageIO.write(bi, "png", file);
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

	public void copySpriteToClipboard(IPath_r path, AnimationIndex a, ViewAngle v, int index)
	{
		String filename = SpriteNames.getFilename(a, v, index);
		File file = path.getFile(filename);
		try
		{
			BufferedImage bi = ImageIO.read(file);
			ImageTransfer ti = new ImageTransfer(bi);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ti, null);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem while copying an image to clipboard.");
			e.addInfo("image file", file.toString());
			e.code.print();
			e.user.showMessage();
		}
	}

	public void resizeAllSprites(IPath_r path, IPoint_r newSize)
	{
		try
		{
			tryResizeAllSprites(path, newSize);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private void tryResizeAllSprites(IPath_r path, IPoint_r newSize) throws PrismException
	{
		for (AnimationIndex a : AnimationIndex.values())
		{
			for (ViewAngle v : ViewAngle.values())
			{
				boolean b;
				int i = 0;
				do
				{
					String filename = SpriteNames.getFilename(a, v, i);
					File file = path.getFile(filename);
					b = file.exists();

					if (b)
					{
						resizeSprite(file, newSize);
						++i;
					}
				}
				while (b);
			}
		}
	}

	private void resizeSprite(File file, IPoint_r newSize) throws PrismException
	{
		BufferedImage b1;
		try
		{
			b1 = ImageIO.read(file);
		}
		catch (IOException e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem reading an image.");
			e.addInfo("image file", file.toString());
			throw e;
		}

		if (b1.getWidth() != newSize.getX() || b1.getHeight() != newSize.getY())
		{
			BufferedImage b2 = new BufferedImage(newSize.getX(), newSize.getY(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = b2.createGraphics();
			int x = newSize.getX() / 2 - b1.getWidth() / 2;
			int y = newSize.getY() - b1.getHeight();
			g2d.drawImage(b1, x, y, null);
			g2d.dispose();
			writeImage(file, b2);
		}
	}
}
