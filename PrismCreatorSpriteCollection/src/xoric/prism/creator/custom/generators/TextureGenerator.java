package xoric.prism.creator.custom.generators;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import xoric.prism.creator.common.factory.SuccessMessage;
import xoric.prism.creator.custom.control.SpriteCollectionSpriteNameGenerator;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.creator.custom.model.SpriteCollectionModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Rect;

import com.ryanm.droid.rugl.util.RectanglePacker;

public class TextureGenerator implements Runnable
{
	private SpriteCollectionModel model;
	private ICreatorFrame frame;

	//	private List<ObjectImages> objects;

	private static Thread thread;

	public TextureGenerator(SpriteCollectionModel model, CreatorFrame frame)
	{
		this.model = model;
		this.frame = frame;
	}

	public static void startCreation(SpriteCollectionModel model)
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
		for (ObjectModel m : model.getObjects())
			objects.add(new ObjectImages(new SpriteCollectionSpriteNameGenerator(model.getPath(), m.getName())));

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
		BufferedImage bi = createTexture(bestSolution, objects, n);

		// write the texture
		frame.increaseChapter();
		frame.setAction("Writing texture");
		File targetFile = model.getPath().getFile("texture.png");
		writeImage(targetFile, bi);

		// show success
		showSuccess(targetFile, bi, n);
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

	private BufferedImage createTexture(IOptimizerResults solution, List<ObjectImages> objects, int imageCount) throws PrismException
	{
		IPoint_r size = solution.getActualSize();
		BufferedImage out = new BufferedImage(size.getX(), size.getY(), BufferedImage.TYPE_INT_ARGB);
		RectanglePacker<BufferedImage> packer = solution.getPacker();
		Graphics2D g = out.createGraphics();

		frame.setProgressMax(imageCount);
		int p = 0;

		for (ObjectImages o : objects)
		{
			for (BufferedImage bi : o.getImages())
			{
				frame.setAction("Placing images (" + (p + 1) + " of " + imageCount + ")");

				Rect r = packer.findRectangle(bi);
				if (r == null)
				{
					PrismException e = new PrismException();
					e.user.setText("There was a problem while generating the texture.");
					e.code.setText("a sprite was not assigned to the texture");
					throw e;
				}
				g.drawImage(bi, r.getX(), r.getY(), null);

				frame.setProgress(p++);
			}
		}
		return out;
	}

	private void writeImage(File targetFile, BufferedImage bi) throws PrismException
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(targetFile);
			ImageIO.write(bi, "png", stream);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem while writing the texture.");
			e.addInfo("file", targetFile.toString());
			throw e;
		}
	}

	private void showSuccess(File targetFile, BufferedImage bi, int imageCount)
	{
		SuccessMessage m = new SuccessMessage("texture");
		m.addFile(targetFile);

		m.addInfo("Size", bi.getWidth() + " x " + bi.getHeight());
		m.addInfo("Sprites", String.valueOf(imageCount));
		m.addIcon(targetFile);

		m.showMessage();
	}
}
