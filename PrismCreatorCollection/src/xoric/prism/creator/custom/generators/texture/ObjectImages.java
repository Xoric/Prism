package xoric.prism.creator.custom.generators.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Point;

class ObjectImages
{
	private final ObjectModel model;
	private final SpriteNameGenerator spriteNameGenerator;
	private final List<BufferedImage> images;

	public ObjectImages(ObjectModel model, SpriteNameGenerator spriteNameGenerator)
	{
		this.model = model;
		this.spriteNameGenerator = spriteNameGenerator;
		this.images = new ArrayList<BufferedImage>();
	}

	public ObjectModel getObjectModel()
	{
		return model;
	}

	public int getImageCount()
	{
		return images.size();
	}

	public List<BufferedImage> getImages()
	{
		return images;
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

	public void findHighestDimension(Point max)
	{
		for (int i = 0; i < images.size(); ++i)
		{
			BufferedImage bi = images.get(i);

			if (bi.getWidth() > max.x)
				max.x = bi.getWidth();
			if (bi.getHeight() > max.y)
				max.y = bi.getHeight();
		}
	}

	public BufferedImage getImage(int index)
	{
		return images.get(index);
	}
}
