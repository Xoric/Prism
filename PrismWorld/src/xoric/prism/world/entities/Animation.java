package xoric.prism.world.entities;

/**
 * First eight values are equivalent to {@link move.MoveType}.
 * @author XoricLee
 * @since 23.03.2012, 11:49:14
 */
public enum Animation
{
	// the first eight values are equivalent to world.MoveType; their order is fixed
	WALK(true),
	RUN(true),
	SLIDE(true),
	RAIL(true),
	DRIVE(true),
	SWIM(true),
	FLY(true),
	SNEAK(true),

	// variable animations
	IDLE(true, 1.5f),
	JUMP_UP(false, 0.3f),
	JUMP_DOWN(false, 0.3f),
	DROP_DOWN(false, 0.3f),
	DROP_LOOP(true),
	STAND_UP(false, 0.6f),
	STUN(true),
	SIT_DOWN(false),
	SIT_LOOP(true),
	ALERT(false, 0.7f),
	CAST_TARGET(false),
	CAST_OMNI(false),
	DIE_DOWN(false),
	DIE_LOOP(true),
	HURT(false),
	ATTACK(false),
	CRIT(false),
	BLOCK(false),
	SNEAK_DOWN(false),
	SNEAK_IDLE(true);

	// static code
	public static final Animation[] VALUES = values();
	public static final int COUNT = VALUES.length;

	static
	{
		SNEAK_IDLE.setSubstitute(IDLE);
		WALK.setSubstitute(IDLE);

		RUN.setSubstitute(WALK);
		SLIDE.setSubstitute(WALK);
		RAIL.setSubstitute(WALK);
		DRIVE.setSubstitute(WALK);
		SWIM.setSubstitute(WALK);
		FLY.setSubstitute(WALK);
		SNEAK.setSubstitute(WALK);

		CRIT.setSubstitute(ATTACK);
		CAST_TARGET.setSubstitute(ATTACK);
		CAST_OMNI.setSubstitute(CAST_TARGET);
	}

	// Animation class
	private Animation substitute;
	private final boolean loop;
	private final float defaultDuration;
	private final int defaultDurationMs;

	private Animation(boolean loop, float defaultDuration)
	{
		this.loop = loop;
		this.defaultDuration = defaultDuration;
		this.defaultDurationMs = (int) (1000.0f * defaultDuration);
	}

	private Animation(boolean loop)
	{
		this.loop = loop;
		this.defaultDuration = 1.0f;
		this.defaultDurationMs = (int) (1000.0f * defaultDuration);
	}

	public float getDefaultDuration()
	{
		return defaultDuration;
	}

	public int getDefaultDurationMs()
	{
		return defaultDurationMs;
	}

	public boolean isLoop()
	{
		return loop;
	}

	public Animation getSubstitute()
	{
		return substitute;
	}

	private void setSubstitute(Animation substitute)
	{
		this.substitute = substitute;
	}
}
