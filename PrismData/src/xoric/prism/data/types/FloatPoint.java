package xoric.prism.data.types;

import xoric.prism.data.heap.Heap;
import xoric.prism.data.heap.HeapReader;
import xoric.prism.data.heap.IStackable;

public class FloatPoint implements IStackable, IFloatPoint_r
{
	public float x;
	public float y;

	public FloatPoint()
	{
		x = 0.0f;
		y = 0.0f;
	}

	public FloatPoint(IFloatPoint_r p)
	{
		this.x = p.getX();
		this.y = p.getY();
	}

	public FloatPoint(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString()
	{
		return "x=" + x + ", y=" + y;
	}

	public void copyFrom(IFloatPoint_r p)
	{
		x = p.getX();
		y = p.getY();
	}

	public void add(IFloatPoint_r p)
	{
		x += p.getX();
		y += p.getY();
	}

	public void subtract(IFloatPoint_r p)
	{
		x -= p.getX();
		y -= p.getY();
	}

	public void multiply(float dx, float dy)
	{
		x *= dx;
		y *= dy;
	}

	public void multiply(IFloatPoint_r f)
	{
		x *= f.getX();
		y *= f.getY();
	}

	@Override
	public void appendTo(Heap h)
	{
		h.floats.add(x);
		h.floats.add(y);
	}

	@Override
	public void extractFrom(HeapReader h)
	{
		x = h.getNextFloat();
		y = h.getNextFloat();
	}

	@Override
	public float getX()
	{
		return x;
	}

	@Override
	public float getY()
	{
		return y;
	}

	@Override
	public void extractFrom(Heap h)
	{
		x = h.floats.get(0);
		y = h.floats.get(0);
	}

	public void negate()
	{
		x = -x;
		y = -y;
	}
}
