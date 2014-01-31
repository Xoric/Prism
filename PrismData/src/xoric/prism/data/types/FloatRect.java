package xoric.prism.data.types;

public class FloatRect implements IStackable, IFloatRect_r
{
	protected final FloatPoint topLeft;
	protected final FloatPoint size;
	protected final FloatPoint bottomRight;

	public FloatRect()
	{
		this.topLeft = new FloatPoint();
		this.size = new FloatPoint();
		this.bottomRight = new FloatPoint();
	}

	public FloatRect(float x, float y, float width, float height)
	{
		this.topLeft = new FloatPoint(x, y);
		this.size = new FloatPoint(width, height);
		this.bottomRight = new FloatPoint();
		updateBottomRight();
	}

	public FloatRect(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		this.topLeft = new FloatPoint(topLeft);
		this.size = new FloatPoint(size);
		this.bottomRight = new FloatPoint();
		updateBottomRight();
	}

	public void set(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		this.topLeft.copyFrom(topLeft);
		this.size.copyFrom(size);
		updateBottomRight();
	}

	public void addX(float dx)
	{
		topLeft.x += dx;
		bottomRight.x += dx;
	}

	public void addY(float dy)
	{
		topLeft.y += dy;
		bottomRight.y += dy;
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
	public IFloatPoint_r getBottomRight()
	{
		return bottomRight;
	}

	private void updateBottomRight()
	{
		bottomRight.x = topLeft.x + size.x;
		bottomRight.y = topLeft.y + size.y;
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

	public void copyFrom(IFloatRect_r rect)
	{
		topLeft.copyFrom(rect.getTopLeft());
		size.copyFrom(rect.getSize());
		updateBottomRight();
	}

	public void copyFrom(IFloatPoint_r position, IFloatPoint_r size)
	{
		this.topLeft.copyFrom(position);
		this.size.copyFrom(size);
		updateBottomRight();
	}

	public void multiplyWidth(float f)
	{
		size.x *= f;
		bottomRight.x = topLeft.x + size.x;
	}

	public void setWidth(float w)
	{
		size.x = w;
		bottomRight.x = topLeft.x + w;
	}

	@Override
	public float getWidth()
	{
		return size.x;
	}

	@Override
	public float getHeight()
	{
		return size.y;
	}

	@Override
	public float getX()
	{
		return topLeft.x;
	}

	@Override
	public float getY()
	{
		return topLeft.y;
	}
}
