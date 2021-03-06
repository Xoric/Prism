package xoric.prism.data.physics;

import java.awt.Point;

public enum View8
{
	RIGHT(1, 0), TOP_RIGHT(1, -1), TOP(0, -1), TOP_LEFT(-1, -1), LEFT(-1, 0), BOTTOM_LEFT(-1, 1), BOTTOM(0, 1), BOTTOM_RIGHT(1, 1);

	public static final View8[] VALUES = values();

	private final Point onset;

	private View8(int x, int y)
	{
		this.onset = new Point(x, y);
	}

	public Point getCoordOnset()
	{
		return onset;
	}
}
