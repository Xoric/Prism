package xoric.prism.world.model;

import xoric.prism.world.entities.ViewAngle;

public class AnimationMeta implements IAnimationMeta
{
	private int[] rowLookup;
	private int[] columns;

	private int currentRow; // just for adding

	public AnimationMeta()
	{
		final int n = ViewAngle.COUNT;

		rowLookup = new int[n];
		columns = new int[n];

		for (int i = 0; i < n; ++i)
		{
			rowLookup[i] = -1;
			columns[i] = 0;
		}

		currentRow = 0;
	}

	@Override
	public int getRow(ViewAngle v)
	{
		return rowLookup[v.ordinal()];
	}

	@Override
	public int getColumnCount(ViewAngle v)
	{
		return columns[v.ordinal()];
	}

	public void registerNextViewAngle(ViewAngle v, int columnCount)
	{
		int i = v.ordinal();

		rowLookup[i] = currentRow++;
		columns[i] = columnCount;
	}
}
