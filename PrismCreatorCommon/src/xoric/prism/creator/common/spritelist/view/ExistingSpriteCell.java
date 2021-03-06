package xoric.prism.creator.common.spritelist.view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import xoric.prism.data.types.IPath_r;

public class ExistingSpriteCell extends SpriteCell
{
	private String frameName;

	public ExistingSpriteCell(String filename, int frameIndex)
	{
		super(filename);

		this.frameName = "sprite " + frameIndex;
	}

	@Override
	public String getFrameName()
	{
		return frameName;
	}

	@Deprecated
	public void loadIcon(IPath_r path)
	{
		try
		{
			File file = path.getFile(filename);
			Image img = ImageIO.read(file);
			icon = new ImageIcon(img);
		}
		catch (IOException e)
		{
			icon = null;
		}
	}
}
