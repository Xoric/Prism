package xoric.prism.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.packable.FloatPacker3;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

public class Ruler implements IPackable, IStackable
{
	public float constant;
	public float factor;

	public float calculate(float parentSize)
	{
		return constant + factor * parentSize;
	}

	public void copyFrom(Ruler other)
	{
		constant = other.constant;
		factor = other.factor;
	}

	@Override
	public String toString()
	{
		return "const=" + constant + ", factor=" + factor;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		constant = IntPacker.unpack_s(stream);
		factor = FloatPacker3.unpack_s(stream, 2);
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		IntPacker.pack_s(stream, (int) constant);
		FloatPacker3.pack_s(stream, factor, 2);
	}

	@Override
	public void extractFrom(Heap_in h)
	{
		constant = h.nextInt();
		factor = h.nextFloat();
	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add((int) constant);
		h.floats.add(factor);
	}
}
