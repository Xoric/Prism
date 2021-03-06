package xoric.prism.creator.common.spritelist.view;

import javax.swing.ImageIcon;

import xoric.prism.data.types.IPoint_r;

public abstract class SpriteCell
{
	protected String filename;
	protected ImageIcon icon;
	protected IPoint_r tileSize;

	public SpriteCell(String filename)
	{
		this.filename = filename;
	}

	public void setIcon(ImageIcon icon)
	{
		this.icon = icon;
	}

	public abstract String getFrameName();

	public ImageIcon getIcon()
	{
		return icon;
	}

	public String getFilename()
	{
		return filename;
	}
}
