package xoric.prism.data;

public class Point implements IStackable, IPoint_r
{
	public int x;
	public int y;

	public Point()
	{
		x = 0;
		y = 0;
	}

	public Point(IPoint_r p)
	{
		this.x = p.getX();
		this.y = p.getY();
	}

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void copyFrom(IPoint_r p)
	{
		x = p.getX();
		y = p.getY();
	}

	@Override
	public void appendTo(Heap h)
	{
		h.ints.add(x);
		h.ints.add(y);
	}

	@Override
	public void extractFrom(HeapReader h)
	{
		x = h.getNextInt();
		y = h.getNextInt();
	}

	@Override
	public int getX()
	{
		return x;
	}

	@Override
	public int getY()
	{
		return y;
	}
}
