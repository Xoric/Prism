package xoric.prism.creator.drawer.model;

import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class SpriteNames
{
	public static String getFilename(AnimationIndex a, ViewAngle v, int index)
	{
		return a.toString().toLowerCase() + "." + v.toString().toLowerCase() + "." + index + ".png";
	}
}
