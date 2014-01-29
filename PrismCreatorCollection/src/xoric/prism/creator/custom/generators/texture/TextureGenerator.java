package xoric.prism.creator.custom.generators.texture;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import xoric.prism.creator.common.factory.SuccessMessage;
import xoric.prism.creator.custom.control.CollectionSpriteNameGenerator;
import xoric.prism.creator.custom.model.CollectionModel;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.Heap;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Rect;

import com.ryanm.droid.rugl.util.RectanglePacker;

public class TextureGenerator implements Runnable
{
	private CollectionModel model;
	private ICreatorFrame frame;

	private static Thread thread;

	public TextureGenerator(CollectionModel model, CreatorFrame frame)
	{
		this.model = model;
		this.frame = frame;
	}

	public static void startCreation(CollectionModel model)
	{
		// create frame
		CreatorFrame f = new CreatorFrame();

		// start texture creator
		TextureGenerator t = new TextureGenerator(model, f);
		thread = new Thread(t);
		thread.start();

		// show frame
		f.showFrame();

		// if user hit "abort" then interrupt thread
		if (thread.isAlive())
			thread.interrupt();
	}

	@Override
	public void run()
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
		finally
		{
			frame.closeFrame();
		}
	}

	private void tryCreate() throws PrismException, InterruptedException
	{
		// create an object for each sprite-object
		List<ObjectImages> objects = new ArrayList<ObjectImages>();
		for (ObjectModel m : model.getObjectModels())
			objects.add(new ObjectImages(m, new CollectionSpriteNameGenerator(model.getPath(), m.getName())));

		// calculate number of chapters
		frame.setChapterMax(Optimizer.chapterCount + 2);

		// count total images
		final int n = countImages(objects);
		frame.setProgressMax(n - 1);

		// load images and find highest width and height
		frame.setAction("Loading sprites");
		loadImages(objects);

		// create and start optimizer
		Optimizer o = new Optimizer(objects, frame);
		IOptimizerResults bestSolution = o.start();

		// create the texture
		frame.increaseChapter();
		MetaBlock mb = new MetaBlock(MetaType.COLLECTION, 0);
		BufferedImage bi = createTexture(bestSolution, objects, n, mb);

		// write the texture
		frame.increaseChapter();
		frame.setAction("Writing texture");
		File textureFile = model.getPath().getFile("texture.png");
		writeImage(textureFile, bi);

		// write the MetaFile
		File metaFile = model.getPath().getFile("texture.meta");
		writeMeta(metaFile, mb);

		// show success
		showSuccess(textureFile, bi, n);
	}

	private int countImages(List<ObjectImages> objects)
	{
		int n = 0;

		for (ObjectImages o : objects)
			n += o.countImages();

		return n;
	}

	private void loadImages(List<ObjectImages> objects) throws PrismException
	{
		// create and load all object images
		final int n = objects.size();
		int p = 0;

		for (int i = 0; i < n; ++i)
		{
			ObjectImages o = objects.get(i);
			p += o.loadImages(frame, p);
		}
	}

	private BufferedImage createTexture(IOptimizerResults solution, List<ObjectImages> objects, int imageCount, MetaBlock mb)
			throws PrismException
	{
		IPoint_r size = solution.getActualSize();
		BufferedImage out = new BufferedImage(size.getX(), size.getY(), BufferedImage.TYPE_INT_ARGB);
		RectanglePacker<BufferedImage> packer = solution.getPacker();
		Graphics2D g = out.createGraphics();

		// update meta data: add size
		MetaLine ml = new MetaLine(MetaKey.SIZE);
		Heap h = ml.getHeap();
		h.ints.add(size.getX());
		h.ints.add(size.getY());
		mb.addMetaLine(ml);

		frame.setProgressMax(imageCount);
		int p = 0;

		for (ObjectImages o : objects)
		{
			ObjectModel m = o.getObjectModel();

			// get this objects width and height
			int width = 0;
			int height = 0;
			if (o.getImageCount() > 0)
			{
				BufferedImage bi = o.getImage(0);
				width = bi.getWidth();
				height = bi.getHeight();
			}

			// update meta data: add object
			ml = new MetaLine(MetaKey.ITEM);
			h = ml.getHeap();
			h.texts.add(m.getName());
			h.ints.add(width);
			h.ints.add(height);
			mb.addMetaLine(ml);

			// update meta data: add rects
			ml = new MetaLine(MetaKey.ALT);
			for (int i = 0; i < m.getRectCount(); ++i)
				m.getRect(i).appendTo(ml.getHeap());
			mb.addMetaLine(ml);

			for (BufferedImage bi : o.getImages())
			{
				frame.setAction("Placing sprites (" + (p + 1) + " of " + imageCount + ")");

				Rect r = packer.findRectangle(bi);
				if (r == null)
				{
					PrismException e = new PrismException();
					e.user.setText("There was a problem while generating the texture.");
					e.code.setText("a sprite was not assigned to the texture");
					throw e;
				}

				// update meta data: add variation
				ml = new MetaLine(MetaKey.SUB);
				r.getPosition().appendTo(ml.getHeap());
				mb.addMetaLine(ml);

				// draw sprite to texture
				g.drawImage(bi, r.getX(), r.getY(), null);

				frame.setProgress(p++);
			}
		}
		return out;
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

	private void writeMeta(File metaFile, MetaBlock mb) throws PrismException
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
