package xoric.prism.data.types;

import xoric.prism.data.heap.IStackable_r;

public interface IPoint_r extends IStackable_r
{
	public int getX();

	public int getY();

	public boolean isSquare();

	/**
	 * Calculates and returns the Euclidean distance between this point and the one given as parameter.
	 * @param target
	 * @return float
	 */
	public float calcDistance(IPoint_r target);
}
