package xoric.prism.data.types;

public class FloatRect implements IStackable, IFloatRect_r
{
	public final FloatPoint topLeft;
	public final FloatPoint size;

	public FloatRect()
	{
		this.topLeft = new FloatPoint();
		this.size = new FloatPoint();
	}

	public FloatRect(float x, float y, float width, float height)
	{
		this.topLeft = new FloatPoint(x, y);
		this.size = new FloatPoint(width, height);
	}

	public FloatRect(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		this.topLeft = new FloatPoint(topLeft);
		this.size = new FloatPoint(size);
	}

	public void set(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		this.topLeft.copyFrom(topLeft);
		this.size.copyFrom(size);
	}

	@Override
	public String toString()
	{
		return "x=" + topLeft.x + ", y=" + topLeft.y + ", w=" + size.x + ", h=" + size.y;
	}

	@Override
	public IFloatPoint_r getTopLeft()
	{
		return topLeft;
	}

	@Override
	public IFloatPoint_r getSize()
	{
		return size;
	}

	@Override
	public FloatPoint calcBottomRight()
	{
		FloatPoint p = new FloatPoint(topLeft);
		p.add(size);

		return p;
	}

	@Override
	public void appendTo(Heap h)
	{
		topLeft.appendTo(h);
		size.appendTo(h);
	}

	@Override
	public void extractFrom(HeapReader h)
	{
		topLeft.extractFrom(h);
		size.extractFrom(h);
	}

	@Override
	public void extractFrom(Heap h)
	{
		topLeft.x = h.floats.get(0);
		topLeft.y = h.floats.get(1);
		size.x = h.floats.get(2);
		size.y = h.floats.get(3);
	}
}
