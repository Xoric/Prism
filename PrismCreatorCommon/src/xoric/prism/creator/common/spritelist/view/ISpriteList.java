package xoric.prism.creator.common.spritelist.view;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.data.types.IPoint_r;

public interface ISpriteList
{
	public void setSpriteSize(IPoint_r tileSize);

	public void loadAndDisplaySprites(SpriteNameGenerator spriteNameGenerator);

	public void clear();

	public void setEnabled(boolean b);
}
