package xoric.prism.creator.custom.model;

public class SpriteNames
{
	public static String getFilename(int spriteIndex, int variation)
	{
		return "sprite" + spriteIndex + ".var" + variation + ".png";
	}
}
