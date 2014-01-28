package xoric.prism.creator.custom.creators;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.custom.SpriteCollectionSpriteNameGenerator;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.creator.custom.model.SpriteCollectionModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Point;

public class TextureCreator implements Runnable
{
	private SpriteCollectionModel model;
	private ICreatorFrame frame;

	//	private List<ObjectImages> objects;

	private static Thread thread;

	public TextureCreator(SpriteCollectionModel model, CreatorFrame frame)
	{
		this.model = model;
		this.frame = frame;
	}

	public static void startCreation(SpriteCollectionModel model)
	{
		// create frame
		CreatorFrame f = new CreatorFrame();

		// start texture creator
		TextureCreator t = new TextureCreator(model, f);
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

	private void tryCreate() throws PrismException
	{
		// create an object for each sprite-object
		List<ObjectImages> objects = new ArrayList<ObjectImages>();
		for (ObjectModel m : model.getObjects())
			objects.add(new ObjectImages(new SpriteCollectionSpriteNameGenerator(model.getPath(), m.getName())));

		// count total images
		final int n = countImages(objects);
		frame.setProgressMax(n - 1);

		// load images and find highest width and height
		frame.setAction("Loading sprites");
		loadImages(objects);
		Point max = findHighestDimensions(objects);

		System.out.println("highest dimensions: " + max.x + ", " + max.y);

		int w = calcNextPowerOfTwo(max.x);
		int h = calcNextPowerOfTwo(max.y);

		System.out.println("image size: " + w + ", " + h);
	}

	private static int calcNextPowerOfTwo(int v)
	{
		int m = 1;
		while (v > m)
			m = m << 1;
		return m;
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

	private Point findHighestDimensions(List<ObjectImages> objects)
	{
		Point max = new Point();

		for (ObjectImages o : objects)
			o.findHighestDimensions(max);

		return max;
	}
}