package xoric.prism.world.animations;

import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaLine_in;

/**
 * First eight values are equivalent to {@link xoric.prism.world.movement.MoveType}.
 * @author XoricLee
 * @since 23.03.2012, 11:49:14
 */
public enum AnimationIndex
{
	// the first eight values are equivalent to movement types:
	WALK(true),
	RUN(true),
	SNEAK(true),
	FLY(true),
	SWIM(true),
	DIVE(true),
	// --
	UNUSED0(false),
	UNUSED1(false),

	// idle animations:
	IDLE(true),
	REST(true),
	CROUCH(true),
	HANG(true),
	FLOAT(true),
	FLOW(true),
	// --
	UNUSED2(false),
	UNUSED3(false),
	UNUSED4(false),
	UNUSED5(false),
	UNUSED6(false),
	UNUSED7(false),
	UNUSED8(false),
	UNUSED9(false),
	UNUSED10(false),
	UNUSED11(false),
	UNUSED12(false),
	UNUSED13(false),
	UNUSED14(false),
	UNUSED15(false),
	UNUSED16(false),
	UNUSED17(false),
	UNUSED18(false),
	UNUSED19(false),
	UNUSED20(false),

	// alternative movement animations:
	SLIDE(true),
	DASH(true),
	JUMP(false),
	// --
	UNUSED21(false),
	UNUSED22(false),
	UNUSED23(false),
	UNUSED24(false),
	UNUSED25(false),
	UNUSED26(false),
	UNUSED27(false),
	UNUSED28(false),
	UNUSED29(false),
	UNUSED30(false),
	UNUSED31(false),
	UNUSED32(false),
	UNUSED33(false),
	UNUSED34(false),
	UNUSED35(false),
	UNUSED36(false),
	UNUSED37(false),
	UNUSED38(false),
	UNUSED39(false),
	UNUSED40(false),

	// social animations:
	ALERT(false),
	// --
	UNUSED41(false),
	UNUSED42(false),
	UNUSED43(false),
	UNUSED44(false),
	UNUSED45(false),
	UNUSED46(false),
	UNUSED47(false),
	UNUSED48(false),
	UNUSED49(false),
	UNUSED50(false),
	UNUSED51(false),
	UNUSED52(false),
	UNUSED53(false),
	UNUSED54(false),
	UNUSED55(false),
	UNUSED56(false),
	UNUSED57(false),
	UNUSED58(false),
	UNUSED59(false),
	UNUSED60(false),
	UNUSED61(false),
	UNUSED62(false),
	UNUSED63(false),
	UNUSED64(false),
	UNUSED65(false),
	UNUSED66(false),
	UNUSED67(false),
	UNUSED68(false),
	UNUSED69(false),
	UNUSED70(false),

	// combat animations:
	ATTACK(false),
	CRIT(false),
	CAST(false),
	CONJURE(false),
	BLOCK(false),
	HURT(false),
	STUNNED(true),
	DIE(true),
	// --
	UNUSED71(false),
	UNUSED72(false),
	UNUSED73(false),
	UNUSED74(false),
	UNUSED75(false),
	UNUSED76(false),
	UNUSED77(false),
	UNUSED78(false),
	UNUSED79(false),
	UNUSED80(false),
	UNUSED81(false),
	UNUSED82(false),
	UNUSED83(false),
	UNUSED84(false),
	UNUSED85(false),
	UNUSED86(false),
	UNUSED87(false),
	UNUSED88(false),
	UNUSED89(false),
	UNUSED90(false),
	UNUSED91(false),
	UNUSED92(false),
	UNUSED93(false),
	UNUSED94(false),
	UNUSED95(false),
	UNUSED96(false),
	UNUSED97(false),
	UNUSED98(false),
	UNUSED99(false),
	UNUSED100(false);

	public static final int COUNT = values().length;
	private static final AnimationIndex[] values = values();

	static
	{
		CROUCH.setSubstitute(IDLE);
		WALK.setSubstitute(IDLE);

		RUN.setSubstitute(WALK);
		//		SLIDE.setSubstitute(WALK);
		//		RAIL.setSubstitute(WALK);
		//		DRIVE.setSubstitute(WALK);
		//		SWIM.setSubstitute(WALK);
		//		FLY.setSubstitute(WALK);
		//		SNEAK.setSubstitute(WALK);

		CRIT.setSubstitute(ATTACK);
		CAST.setSubstitute(ATTACK);
		CONJURE.setSubstitute(CAST);
	}

	public static AnimationIndex valueOf(int i)
	{
		// TODO: make safe
		return values[i];
	}

	private final boolean loop;
	private AnimationIndex substitute;
	private String description;
	private int priority;

	private AnimationIndex(boolean loop)
	{
		this.loop = loop;
		this.description = "";
	}

	public int getPriority()
	{
		return priority;
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

	private static boolean loadDevInfo(Heap_in h)
	{
		boolean isOK;
		try
		{
			String s = h.texts.get(0).toString();
			AnimationIndex a = valueOf(s);

			a.description = h.texts.get(1).toString();
			a.priority = h.ints.get(0);

			isOK = true;
		}
		catch (Exception e)
		{
			isOK = false;
		}
		return isOK;
	}

	public static boolean loadDevInfoAll(MetaBlock_in metaBlock)
	{
		boolean isOK = true;

		for (MetaLine_in metaLine : metaBlock.getMetaLines())
		{
			Heap_in h = metaLine.getHeap();
			if (h.texts.size() == 2 && h.ints.size() == 1)
				isOK &= loadDevInfo(h);
		}
		return isOK;
	}
}
