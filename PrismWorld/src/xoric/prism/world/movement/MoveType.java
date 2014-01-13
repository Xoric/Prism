package xoric.prism.world.movement;

/**
 * Equivalent to {@link xoric.prism.world.animations.AnimationIndex}.
 * @author XoricLee
 * @since 30.10.2011, 13:52:36
 */
public enum MoveType
{
	// limited to eight, equivalent to the the first eight animations
	// revised:
	WALK,
	RUN,
	CROUCH,
	SLIDE,
	SWIM,
	FLY,
	// unsure:
	RIDE,
	RAIL;

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
