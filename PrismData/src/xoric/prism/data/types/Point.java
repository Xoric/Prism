package xoric.prism.data.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

public class Point implements IStackable, IPackable, IPoint_r
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
		this.x = p == null ? 0 : p.getX();
		this.y = p == null ? 0 : p.getY();
	}

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void copyFrom(IPoint_r p)
	{
		x = p == null ? 0 : p.getX();
		y = p == null ? 0 : p.getY();
	}

	@Override
	public float calcDistance(IPoint_r target)
	{
		int dx = x - target.getX();
		int dy = y - target.getY();
		int i = dx * dx + dy * dy;
		float cost = (float) Math.sqrt(i);
		return cost;
	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add(x);
		h.ints.add(y);
	}

	@Override
	public void extractFrom(Heap_in h)
	{
		x = h.nextInt();
		y = h.nextInt();
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

	@Override
	public String toString()
	{
		return "x=" + x + ", y=" + y;
	}

	@Override
	public boolean equals(Object o)
	{
		boolean b = false;

		if (o instanceof IPoint_r)
		{
			IPoint_r p = (IPoint_r) o;
			b = x == p.getX() && y == p.getY();
		}
		return b;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, x);
		IntPacker.pack_s(stream, y);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		x = IntPacker.unpack_s(stream);
		y = IntPacker.unpack_s(stream);
	}

	@Override
	public boolean isSquare()
	{
		return x == y;
	}
}
