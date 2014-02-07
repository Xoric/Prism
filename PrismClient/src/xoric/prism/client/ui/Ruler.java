package xoric.prism.client.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.FloatPacker3;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

public class Ruler implements IPackable
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
}
