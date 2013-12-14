package xoric.prism.data;

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
}