package xoric.prism.creator.custom.control;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IText_r;

public class CollectionSpriteNameGenerator extends SpriteNameGenerator
{
	public CollectionSpriteNameGenerator(IPath_r path, IText_r objectName)
	{
		super(path, objectName.toString().toLowerCase() + ".var", ".png");
	}
}
