package xoric.prism.creator.custom.creators;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Point;

class ObjectImages
{
	private final SpriteNameGenerator spriteNameGenerator;
	private final List<BufferedImage> images;

	public ObjectImages(SpriteNameGenerator spriteNameGenerator)
	{
		this.spriteNameGenerator = spriteNameGenerator;
		this.images = new ArrayList<BufferedImage>();
	}

	public int countImages()
	{
		int i = 0;
		boolean resume;

		do
		{
			File f = spriteNameGenerator.getFile(i);
			resume = f.exists();

			if (resume)
				++i;
		}
		while (resume);

		return i;
	}

	public int loadImages(ICreatorFrame frame, int startProgress) throws PrismException
	{
		int i = 0;
		boolean resume;

		images.clear();

		do
		{
			File f = spriteNameGenerator.getFile(i);
			resume = f.exists();

			if (resume)
			{
				try
				{
					BufferedImage bi = ImageIO.read(f);
					images.add(bi);

					frame.setProgress(startProgress + i);
				}
				catch (IOException e0)
				{
					PrismException e = new PrismException(e0);
					e.setText("Image could not be read.");
					e.addInfo("image", f.toString());
					throw e;
				}
				++i;
			}
		}
		while (resume);

		return i;
	}

	public void findHighestDimensions(Point max)
	{
		for (int i = 0; i < images.size(); ++i)
		{
			BufferedImage bi = images.get(i);

			if (bi.getWidth() > max.getX())
				max.x = bi.getWidth();
			if (bi.getHeight() > max.getY())
				max.y = bi.getHeight();
		}
	}
}