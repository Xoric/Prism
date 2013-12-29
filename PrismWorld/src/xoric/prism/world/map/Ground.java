package xoric.prism.world.map;

import xoric.prism.world.movement.MoveCaps;

public class Ground
{
	private final int index;
	private MoveCaps moveCaps;

	public Ground(int index)
	{
		this.index = index;
	}

	public MoveCaps getMoveCaps()
	{
		return moveCaps;
	}

	public int getIndex()
	{
		return index;
	}
}
