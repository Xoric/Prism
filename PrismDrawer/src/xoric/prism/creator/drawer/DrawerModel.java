package xoric.prism.creator.drawer;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class DrawerModel
{
	private Image image;

	public void createImage(int width, int height)
	{
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
}
