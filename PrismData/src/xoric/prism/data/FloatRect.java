package xoric.prism.data;

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
}
