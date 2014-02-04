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

	public FloatRect(IFloatRect_r other)
	{
		this.topLeft = new FloatPoint(other.getTopLeft());
		this.size = new FloatPoint(other.getSize());
		this.bottomRight = new FloatPoint(other.getBottomRight());
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

	public void setTopLeft(IFloatPoint_r pos)
	{
		topLeft.copyFrom(pos);
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

	public void setX(float x)
	{
		topLeft.x = x;
		bottomRight.x = x + size.x;
	}

	public void setY(float y)
	{
		topLeft.y = y;
		bottomRight.y = y + size.y;
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

	@Override
	public float getRight()
	{
		return bottomRight.getX();
	}

	@Override
	public float getBottom()
	{
		return bottomRight.getY();
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

	public void multiplyHeight(float f)
	{
		size.y *= f;
		bottomRight.y = topLeft.y + size.y;
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

	public void setSize(IFloatPoint_r spriteSize)
	{
		size.copyFrom(spriteSize);
		updateBottomRight();
	}

	public void setSize(float w, float h)
	{
		size.x = w;
		size.y = h;
		updateBottomRight();
	}

	public void setWidth(float w)
	{
		size.x = w;
		bottomRight.x = topLeft.x + w;
	}

	public void setHeight(float h)
	{
		size.y = h;
		bottomRight.y = topLeft.y + h;
	}

	public void set(float x, float y, float w, float h)
	{
		topLeft.x = x;
		topLeft.y = y;
		size.x = w;
		size.y = h;
		updateBottomRight();
	}
}
