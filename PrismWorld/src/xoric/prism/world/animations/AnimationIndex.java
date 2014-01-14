package xoric.prism.world.animations;

import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.types.Heap;

/**
 * First eight values are equivalent to {@link xoric.prism.world.movement.MoveType}.
 * @author XoricLee
 * @since 23.03.2012, 11:49:14
 */
public enum AnimationIndex
{
	// the first eight values are equivalent to movement types
	// revised:
	WALK(true),
	RUN(true),
	CROUCH(true),
	SLIDE(true),
	SWIM(true),
	FLY(true),
	// unsure:
	RIDE(true),
	RAIL(true),

	// the second eight values are the appropriate idle animations for all movement types
	IDLE(true),
	IDLE_SWIM(true),
	IDLE_FLY(true),
	// revised:
	ATTACK(false),
	ALERT(false),

	// variable animations
	// revised:
	// unsure:
	JUMP_UP(false),
	JUMP_DOWN(false),
	DROP_DOWN(false),
	DROP_LOOP(true),
	STAND_UP(false),
	STUN(true),
	SIT_DOWN(false),
	SIT_LOOP(true),
	CAST_TARGET(false),
	CAST_OMNI(false),
	DIE_DOWN(false),
	DIE_LOOP(true),
	HURT(false),
	CRIT(false),
	BLOCK(false),
	SNEAK_DOWN(false),
	SNEAK_IDLE(true);

	public static final int COUNT = values().length;
	private static final AnimationIndex[] values = values();

	static
	{
		SNEAK_IDLE.setSubstitute(IDLE);
		WALK.setSubstitute(IDLE);

		//		RUN.setSubstitute(WALK);
		//		SLIDE.setSubstitute(WALK);
		//		RAIL.setSubstitute(WALK);
		//		DRIVE.setSubstitute(WALK);
		//		SWIM.setSubstitute(WALK);
		//		FLY.setSubstitute(WALK);
		//		SNEAK.setSubstitute(WALK);

		CRIT.setSubstitute(ATTACK);
		CAST_TARGET.setSubstitute(ATTACK);
		CAST_OMNI.setSubstitute(CAST_TARGET);
	}

	public static AnimationIndex valueOf(int i)
	{
		return values[i];
	}

	private final boolean loop;
	private AnimationIndex substitute;
	private String description;

	private AnimationIndex(boolean loop)
	{
		this.loop = loop;
		this.description = "";
	}

	public String getDescription()
	{
		return description;
	}

	public boolean isLoop()
	{
		return loop;
	}

	public AnimationIndex getSubstitute()
	{
		return substitute;
	}

	private void setSubstitute(AnimationIndex substitute)
	{
		this.substitute = substitute;
	}

	private static boolean loadDescription(Heap h)
	{
		boolean isOK;
		try
		{
			String s = h.texts.get(0).toString();
			AnimationIndex a = valueOf(s);
			a.description = h.texts.get(1).toString();
			isOK = true;
		}
		catch (Exception e)
		{
			isOK = false;
		}
		return isOK;
	}

	public static boolean loadDescriptions(MetaBlock metaBlock)
	{
		boolean isOK = true;

		for (MetaLine metaLine : metaBlock.getMetaLines())
		{
			Heap h = metaLine.getHeap();
			if (h.texts.size() == 2)
				isOK &= loadDescription(h);
		}
		return isOK;
	}
}