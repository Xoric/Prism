package xoric.prism.world.map;

import xoric.prism.world.movement.MoveCaps;

@Deprecated
public class Ground
{
	private int animationStart;
	private int animationCount;

	private MoveCaps moveCaps;

	public Ground(int index)
	{
		this.animationStart = index;
	}

	public MoveCaps getMoveCaps()
	{
		return moveCaps;
	}

	public int getIndex()
	{
		return animationStart;
	}
}
