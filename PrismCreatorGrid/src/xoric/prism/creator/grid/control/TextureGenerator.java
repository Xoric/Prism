package xoric.prism.creator.grid.control;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import xoric.prism.creator.common.factory.SuccessMessage;
import xoric.prism.creator.grid.model.GridModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;

public class TextureGenerator
{
	private final GridModel model;

	public TextureGenerator(GridModel model)
	{
		this.model = model;
	}

	public void create()
	{
		try
		{
			tryCreate();
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
		catch (Exception e0)
		{
			e0.printStackTrace();
		}
	}

	private void tryCreate() throws PrismException, InterruptedException
	{
		// calculate rows and with to minimize size
		int w = model.getSpriteSize().getX();
		int h = model.getSpriteSize().getY();
		int n = countImages();
		Point counts = calcColumnsAndRows(w, h, n);

		// create the texture
		MetaBlock_out mb = new MetaBlock_out(MetaType.GRID, 0);
		BufferedImage bi = createTexture(counts, n, mb);

		// write the texture
		File textureFile = model.getPath().getFile("texture.png");
		writeImage(textureFile, bi);

		// write the MetaFile
		File metaFile = model.getPath().getFile("texture.meta");
		writeMeta(metaFile, mb);

		// show success
		showSuccess(textureFile, bi, n);
	}

	private int countImages()
	{
		int n = 0;
		while (model.getSpriteNameGenerator().getFile(n).exists())
			++n;

		return n;
	}

	private static int calcNextPowerOfTwo(int v)
	{
		int m = 1;
		while (v > m)
			m = m << 1;

		return m;
	}

	private static Point calcColumnsAndRows(int w, int h, int n)
	{
		int sBest = 0;
		int cBest = 0;
		int rBest = 0;

		for (int c = 1; c < n + 1; ++c)
		{
			int r = n / c + (n % c > 0 ? 1 : 0);
			int s = calcNextPowerOfTwo(c * w) + calcNextPowerOfTwo(r * h);

			if (c == 1 || s < sBest)
			{
				sBest = s;
				cBest = c;
				rBest = r;
			}
		}
		return new Point(cBest, rBest);
	}

	private BufferedImage createTexture(IPoint_r counts, int n, MetaBlock_out mb) throws PrismException
	{
		IPoint_r spriteSize = model.getSpriteSize();
		int width = spriteSize.getX() * counts.getX();
		int height = spriteSize.getY() * counts.getY();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();

		// update meta data: add name
		MetaLine_out ml = new MetaLine_out(MetaKey.ITEM);
		ml.getHeap().texts.add(new Text(model.getName()));
		mb.addMetaLine(ml);

		// update meta data: add texture and sprite size
		ml = new MetaLine_out(MetaKey.SIZE);
		ml.getHeap().ints.add(width);
		ml.getHeap().ints.add(height);
		ml.getHeap().ints.add(spriteSize.getX());
		ml.getHeap().ints.add(spriteSize.getY());
		mb.addMetaLine(ml);

		// update meta data: add column count
		ml = new MetaLine_out(MetaKey.ALT);
		ml.getHeap().ints.add(counts.getX());
		mb.addMetaLine(ml);

		// update meta data: add count
		ml = new MetaLine_out(MetaKey.COUNT);
		ml.getHeap().ints.add(n);
		mb.addMetaLine(ml);

		// update meta data: add hot spots
		// TODO

		for (int i = 0; i < n; ++i)
		{
			BufferedImage bi = loadImage(i);

			int c = i % counts.getX();
			int r = i / counts.getX();
			int x = c * spriteSize.getX() + spriteSize.getX() / 2 - bi.getWidth() / 2;
			int y = (r + 1) * spriteSize.getY() - bi.getHeight();

			g.drawImage(bi, x, y, null);
		}
		return out;
	}

	private BufferedImage loadImage(int index) throws PrismException
	{
		File f = model.getSpriteNameGenerator().getFile(index);
		BufferedImage bi;
		try
		{
			bi = ImageIO.read(f);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("An error occured while trying to read an image");
			e.addInfo("image file", f.toString());
			throw e;
		}
		return bi;
	}

	private void writeImage(File textureFile, BufferedImage bi) throws PrismException
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(textureFile);
			ImageIO.write(bi, "png", stream);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem while writing the texture.");
			e.addInfo("file", textureFile.toString());
			throw e;
		}
	}

	private void writeMeta(File metaFile, MetaBlock_out mb) throws PrismException
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(metaFile);
			mb.pack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem writing information about the generated texture.");
			e.addInfo("file", metaFile.toString());
			throw e;
		}
	}

	private void showSuccess(File textureFile, BufferedImage bi, int imageCount)
	{
		SuccessMessage m = new SuccessMessage("texture");
		m.addFile(textureFile);

		m.addInfo("Size", bi.getWidth() + " x " + bi.getHeight());
		m.addInfo("Sprites", String.valueOf(imageCount));
		m.addIcon(textureFile);

		m.showMessage();
	}
}
