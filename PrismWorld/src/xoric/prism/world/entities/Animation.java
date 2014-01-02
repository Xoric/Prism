package xoric.prism.world.entities;

import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.types.Heap;

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
	IDLE(true),
	IDLE_SWIM(true),
	IDLE_FLY(true),
	JUMP_UP(false),
	JUMP_DOWN(false),
	DROP_DOWN(false),
	DROP_LOOP(true),
	STAND_UP(false),
	STUN(true),
	SIT_DOWN(false),
	SIT_LOOP(true),
	ALERT(false),
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

	public static final int COUNT = values().length;

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
	private final boolean loop;
	private Animation substitute;
	private String description;

	private Animation(boolean loop)
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

	public Animation getSubstitute()
	{
		return substitute;
	}

	private void setSubstitute(Animation substitute)
	{
		this.substitute = substitute;
	}

	private static boolean loadDescription(Heap h)
	{
		boolean isOK;
		try
		{
			String s = h.texts.get(0).toString();
			Animation a = valueOf(s);
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
