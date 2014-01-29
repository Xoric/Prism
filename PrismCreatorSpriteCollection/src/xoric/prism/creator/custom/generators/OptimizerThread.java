package xoric.prism.creator.custom.generators;

import java.awt.image.BufferedImage;
import java.util.List;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Rect;

import com.ryanm.droid.rugl.util.RectanglePacker;

class OptimizerThread extends Thread implements IOptimizerResults
{
	private final List<ObjectImages> objects;
	private RectanglePacker<BufferedImage> packer;
	private final IPoint_r originalSize;
	private volatile Point actualSize;

	public OptimizerThread(List<ObjectImages> objects, IPoint_r size)
	{
		this.objects = objects;
		this.originalSize = new Point(size);
		this.packer = new RectanglePacker<BufferedImage>(originalSize.getX(), originalSize.getY(), 0);
	}

	@Override
	public void run()
	{
		for (ObjectImages o : objects)
			for (BufferedImage bi : o.getImages())
				packer.insert(bi.getWidth(), bi.getHeight(), bi);

		int maxWidth = 0;
		int maxHeight = 0;

		for (ObjectImages o : objects)
		{

			for (BufferedImage bi : o.getImages())
			{
				Rect r = packer.findRectangle(bi);

				if (r != null)
				{
					int x2 = r.calcX2();
					int y2 = r.calcY2();

					if (x2 > maxWidth)
						maxWidth = x2;
					if (y2 > maxWidth)
						maxHeight = y2;
				}
				else
					return;
			}
		}
		actualSize = new Point(maxWidth, maxHeight);
	}

	@Override
	public boolean wasSuccessful()
	{
		return actualSize != null;
	}

	@Override
	public IPoint_r getOriginalSize()
	{
		return originalSize;
	}

	@Override
	public Point getActualSize()
	{
		return actualSize;
	}

	@Override
	public boolean isBetterThan(IOptimizerResults other)
	{
		if (actualSize != null)
			return false;

		IPoint_r s = other.getActualSize();

		return !other.wasSuccessful() || actualSize.getX() * actualSize.getY() < s.getX() * s.getY();
	}

	@Override
	public RectanglePacker<BufferedImage> getPacker()
	{
		return packer;
	}
}
