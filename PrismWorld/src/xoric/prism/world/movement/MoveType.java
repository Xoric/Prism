package xoric.prism.world.movement;

/**
 * Equivalent to {@link entity.anim.Animation}.
 * @author XoricLee
 * @since 30.10.2011, 13:52:36
 */
public enum MoveType
{
	// limited to eight, equivalent to the the first eight values in animation.Animation thus fixed in chronology
	WALK,
	RUN,
	HOVER,
	RAIL,
	DRIVE,
	SWIM,
	FLY,
	SNEAK;

	public static final MoveType[] VALUES = values();
	public static final String[] NAMES;
	public static final int COUNT = VALUES.length;

	static
	{
		int i = 0;
		NAMES = new String[COUNT];
		for (MoveType t : VALUES)
			NAMES[i++] = t.toString();
	}
}