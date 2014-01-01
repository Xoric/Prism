package xoric.prism.creator.drawer;

import java.awt.Image;
import java.awt.image.BufferedImage;

import xoric.prism.data.Point;
import xoric.prism.data.Text;

public class DrawerModel
{
	private Image image;

	private boolean hasChanges;

	private Text name;
	private Point tileSize;

	public DrawerModel()
	{
		name = new Text("NEW MODEL");
		tileSize = new Point(20, 30);
	}

	public void setChanged()
	{
		hasChanges = true;
	}

	public boolean hasChanges()
	{
		return hasChanges;
	}

	public Text getName()
	{
		return name;
	}

	public Point getTileSize()
	{
		return tileSize;
	}

	public void createImage(int width, int height)
	{
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
}
