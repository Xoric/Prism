package xoric.prism.data;

public class Rect implements IRect_r, IStackable
{
	private final Point position;
	private final Point size;

	public Rect()
	{
		position = new Point();
		size = new Point();
	}

	public Rect(IPoint_r position, IPoint_r size)
	{
		this.position = new Point(position.getX(), position.getY());
		this.size = new Point(size.getX(), size.getY());
	}

	public Rect(int x, int y, int width, int height)
	{
		this.position = new Point(x, y);
		this.size = new Point(width, height);
	}

	public void setPosition(int x, int y)
	{
		position.x = x;
		position.y = y;
	}

	public void setPosition(IPoint_r position)
	{
		this.position.copyFrom(position);
	}

	public void setSize(int width, int height)
	{
		size.x = width;
		size.y = height;
	}

	public void setSize(IPoint_r size)
	{
		this.size.copyFrom(size);
	}

	@Override
	public int getX()
	{
		return position.x;
	}

	@Override
	public int getY()
	{
		return position.y;
	}

	@Override
	public int getWidth()
	{
		return size.x;
	}

	@Override
	public int getHeight()
	{
		return size.y;
	}

	@Override
	public int calcX2()
	{
		return position.x + size.x;
	}

	@Override
	public int calcY2()
	{
		return position.y + size.y;
	}

	@Override
	public void appendTo(Heap h)
	{
		position.appendTo(h);
		size.appendTo(h);
	}

	@Override
	public void extractFrom(HeapReader h)
	{
		position.extractFrom(h);
		size.extractFrom(h);
	}

	@Override
	public IPoint_r getPosition()
	{
		return position;
	}

	@Override
	public IPoint_r getSize()
	{
		return size;
	}
}
