package xoric.prism.world.entities;

public enum ViewAngle
{
	RIGHT, TOP_RIGHT, TOP, TOP_LEFT, LEFT, BOT_LEFT, BOT, BOT_RIGHT;

	private static final ViewAngle[] values = values();

	public static ViewAngle valueOf(int index)
	{
		return values[index];
	}
}
