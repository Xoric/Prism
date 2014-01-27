package xoric.prism.creator.common.spritelist.view;

import javax.swing.ImageIcon;

public class NewSpriteCell extends SpriteCell
{
	public NewSpriteCell(ImageIcon icon)
	{
		super("");

		setIcon(icon);
	}

	@Override
	public String getFrameName()
	{
		return "+";
	}
}
