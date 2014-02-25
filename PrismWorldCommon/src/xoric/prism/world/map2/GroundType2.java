package xoric.prism.world.map2;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.world.movement.MoveCaps;

/**
 * @author XoricLee
 * @since 23.02.2014, 18:33:41
 */
public class GroundType2 implements IStackable
{
	private final int index;

	protected int animationStart;
	protected int animationCount;
	protected int animationInterval;
	protected final MoveCaps moveCaps;

	public GroundType2(int index)
	{
		this.index = index;
		this.moveCaps = new MoveCaps();
	}

	public void initialize(int animationStart, int animationCount, int animationInterval)
	{
		this.animationStart = animationStart;
		this.animationCount = animationCount;
		this.animationInterval = animationInterval;
	}

	public int getIndex()
	{
		return index;
	}

	public MoveCaps getMoveCaps()
	{
		return moveCaps;
	}

	public int getAnimationStart()
	{
		return animationStart;
	}

	public int getAnimationCount()
	{
		return animationCount;
	}

	public int getAnimationInterval()
	{
		return animationInterval;
	}

	// IStackable_out:
	@Override
	public void appendTo(Heap_out h)
	{
		// version
		final int version = 0;
		h.ints.add(version);

		// data
		h.ints.add(animationStart);
		h.ints.add(animationCount);
		h.ints.add(animationInterval);
		moveCaps.appendTo(h);
	}

	// IStackable_in:
	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		// version
		final int version = h.nextInt();

		// data
		animationStart = h.nextInt();
		animationCount = h.nextInt();
		animationInterval = h.nextInt();
		moveCaps.extractFrom(h);
	}
}
