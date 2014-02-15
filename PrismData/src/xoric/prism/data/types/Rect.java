package xoric.prism.data.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.packable.IPackable;

public class Rect implements IRect_r, IStackable, IPackable
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

	@Override
	public String toString()
	{
		return "x=" + position.x + ", y=" + position.y + ", w=" + size.x + ", h=" + size.y;
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
	public void appendTo(Heap_out h)
	{
		position.appendTo(h);
		size.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h)
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

	@Override
	public float calcDistance(IPoint_r target)
	{
		return position.calcDistance(target);
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		position.unpack(stream);
		size.unpack(stream);
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		position.pack(stream);
		size.pack(stream);
	}

	@Override
	public boolean isSquare()
	{
		return size.isSquare();
	}
}
