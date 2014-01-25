package xoric.prism.creator.drawer.model;

import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class SpriteNames
{
	public static String getFilename(AnimationIndex a, int variation, ViewAngle v, int index)
	{
		return a.toString().toLowerCase() + ".var" + variation + "." + v.toString().toLowerCase() + "." + index + ".png";
	}
}
