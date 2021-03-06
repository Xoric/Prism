package xoric.prism.creator.models.image;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationSpriteNameGenerator extends SpriteNameGenerator
{
	public AnimationSpriteNameGenerator(IPath_r path, AnimationIndex a, int variation, ViewAngle v)
	{
		super(path, getPrefix(a, variation, v), ".png");
	}

	private static String getPrefix(AnimationIndex a, int variation, ViewAngle v)
	{
		return a.toString().toLowerCase() + ".var" + variation + "." + v.toString().toLowerCase() + ".";
	}

	public static String getFilename(AnimationIndex a, int variation, ViewAngle v, int index)
	{
		return getPrefix(a, variation, v) + index + ".png";
	}
}
