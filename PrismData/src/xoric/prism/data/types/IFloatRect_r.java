package xoric.prism.data.types;

import xoric.prism.data.heap.IStackable_out;

public interface IFloatRect_r extends IStackable_out
{
	public IFloatPoint_r getTopLeft();

	public IFloatPoint_r getSize();

	public IFloatPoint_r getBottomRight();

	public float getWidth();

	public float getHeight();

	public float getX();

	public float getY();

	public float getRight();

	public float getBottom();
}
