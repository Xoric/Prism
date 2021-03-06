package xoric.prism.creator.common.spritelist.control;

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

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

public abstract class SpriteListControl
{
	private static int findHighestIndex(SpriteNameGenerator spriteNameGenerator)
	{
		int n = 0;
		boolean b;
		do
		{
			File f = spriteNameGenerator.getFile(n);
			b = f.exists();
			if (b)
				++n;
		}
		while (b);
		return n - 1;
	}

	private static void renameFiles(SpriteNameGenerator spriteNameGenerator, int highest, int lowest) throws PrismException
	{
		File f1 = null;
		File f2 = null;
		try
		{
			for (int i = highest; i >= lowest; --i)
			{
				f1 = spriteNameGenerator.getFile(i);
				f2 = spriteNameGenerator.getFile(i + 1);
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

	private static boolean increaseFileIndices(SpriteNameGenerator spriteNameGenerator, int index)
	{
		boolean b;

		// find highest existing index
		int highest = findHighestIndex(spriteNameGenerator);

		// increase all indices equal or greater than the given parameter by one
		try
		{
			renameFiles(spriteNameGenerator, highest, index);
			b = true;
		}
		catch (PrismException e)
		{
			b = false;
			e.user.showMessage();
		}
		return b;
	}

	private static void writeImage(SpriteNameGenerator spriteNameGenerator, int index, BufferedImage bi) throws PrismException
	{
		writeImage(spriteNameGenerator.getFile(index), bi);
	}

	private static void writeImage(File file, BufferedImage bi) throws PrismException
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

	private static void createNewSprite(SpriteNameGenerator spriteNameGenerator, IPoint_r size, int index) throws PrismException
	{
		BufferedImage img = new BufferedImage(size.getX(), size.getY(), BufferedImage.TYPE_INT_ARGB);
		writeImage(spriteNameGenerator, index, img);
	}

	private static void makeTransparent(BufferedImage bi)
	{
		int w = bi.getWidth();
		int h = bi.getHeight();
		int tc = bi.getRGB(0, 0);

		for (int y = 0; y < h; ++y)
			for (int x = 0; x < w; ++x)
				if (bi.getRGB(x, y) == tc)
					bi.setRGB(x, y, 0);
	}

	private static void tryMakeSpritesTransparent(SpriteNameGenerator spriteNameGenerator, List<Integer> indices) throws PrismException
	{
		File file = null;
		int i = 0;
		boolean b;

		do
		{
			file = spriteNameGenerator.getFile(i);
			b = file.exists();
			if (b)
			{
				if (indices.contains(i))
				{
					try
					{
						BufferedImage bi = ImageIO.read(file);
						makeTransparent(bi);
						writeImage(file, bi);
					}
					catch (IOException e0)
					{
						PrismException e = new PrismException(e0);
						e.setText("An error occured while trying to make a sprite transparent.");
						e.addInfo("image file", file.toString());
						throw e;
					}
				}
			}
			++i;
		}
		while (b);
	}

	private static BufferedImage embedClipboard(Image clipboardImage, IPoint_r size)
	{
		BufferedImage bi = new BufferedImage(clipboardImage.getWidth(null), clipboardImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(clipboardImage, 0, 0, null);
		g2d.dispose();

		makeTransparent(bi);

		if (size == null)
			size = new Point(bi.getWidth(), bi.getHeight());

		BufferedImage img = new BufferedImage(size.getX(), size.getY(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		int x = size.getX() / 2 - bi.getWidth() / 2;
		int y = size.getY() - bi.getHeight();
		g.drawImage(bi, x, y, null);
		g.dispose();

		return img;
	}

	public static void requestCloneSprite(SpriteNameGenerator spriteNameGenerator, int index)
	{
		// load sprite image
		File file = spriteNameGenerator.getFile(index);
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

		// increase indices
		boolean b = increaseFileIndices(spriteNameGenerator, index);
		if (!b)
			return;

		// insert sprite
		try
		{
			writeImage(spriteNameGenerator, index, bi);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	public static void requestInsertNewSprite(SpriteNameGenerator spriteNameGenerator, IPoint_r size, int index)
	{
		// increase indices
		boolean b = increaseFileIndices(spriteNameGenerator, index);
		if (!b)
			return;

		// create new sprite
		try
		{
			createNewSprite(spriteNameGenerator, size, index);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	/**
	 * Inserts a sprite from clipboard.
	 * @param spriteNameGenerator
	 * @param size
	 *            Specify a size if the image read from clipboard should be resized. Set this parameter to null if the original image size
	 *            should be kept.
	 * @param index
	 */
	public static void requestInsertSpriteFromClipboard(SpriteNameGenerator spriteNameGenerator, IPoint_r size, int index)
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

		// increase indices
		boolean b = increaseFileIndices(spriteNameGenerator, index);
		if (!b)
			return;

		// insert sprite
		try
		{
			BufferedImage bi = embedClipboard(clipboardImage, size);
			writeImage(spriteNameGenerator, index, bi);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	public static void requestDeleteSprites(SpriteNameGenerator spriteNameGenerator, List<Integer> indices)
	{
		int r = JOptionPane.showConfirmDialog(null, "Please confirm deleting the selected sprite(s).", "Delete sprites",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (r != JOptionPane.YES_OPTION)
			return;

		File file = null;
		int i = 0;
		int gap = 0;
		boolean b;

		do
		{
			file = spriteNameGenerator.getFile(i);
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
						File file2 = spriteNameGenerator.getFile(i - gap);

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

	public static void cloneSprite(SpriteNameGenerator spriteNameGenerator, int index)
	{
		// load sprite image
		File file = spriteNameGenerator.getFile(index);
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

		// increase indices
		boolean b = increaseFileIndices(spriteNameGenerator, index);
		if (!b)
			return;

		// insert sprite
		try
		{
			writeImage(spriteNameGenerator, index, bi);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	public static void requestCopySpriteToClipboard(SpriteNameGenerator spriteNameGenerator, int index)
	{
		File f = spriteNameGenerator.getFile(index);
		try
		{
			BufferedImage bi = ImageIO.read(f);
			ImageTransfer ti = new ImageTransfer(bi);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ti, null);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem while copying an image to clipboard.");
			e.addInfo("image file", f.toString());
			e.code.print();
			e.user.showMessage();
		}
	}

	public static void requestMakeSpritesTransparent(SpriteNameGenerator spriteNameGenerator, List<Integer> indices)
	{
		try
		{
			tryMakeSpritesTransparent(spriteNameGenerator, indices);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}
}
