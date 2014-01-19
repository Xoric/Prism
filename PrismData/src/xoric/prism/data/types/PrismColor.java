package xoric.prism.data.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;

public class PrismColor implements IPackable
{
	private final float[] rgba_f;
	private final int[] rgba_i;

	public PrismColor()
	{
		rgba_f = new float[4];
		rgba_i = new int[4];
	}

	public float[] getRGBA()
	{
		return rgba_f;
	}

	public void set(int r, int g, int b, int a)
	{
		rgba_i[0] = r;
		rgba_i[1] = g;
		rgba_i[2] = b;
		rgba_i[3] = a;

		copyIntsToFloats();
	}

	public void set(float r, float g, float b, float a)
	{
		rgba_f[0] = r;
		rgba_f[1] = g;
		rgba_f[2] = b;
		rgba_f[3] = a;

		rgba_i[0] = (int) (255 * r);
		rgba_i[1] = (int) (255 * g);
		rgba_i[2] = (int) (255 * b);
		rgba_i[3] = (int) (255 * a);
	}

	private void copyIntsToFloats()
	{
		for (int i = 0; i < 4; ++i)
			rgba_f[i] = rgba_i[i] / 255.0f;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		IntPacker.unpack_s(stream, rgba_i);
		copyIntsToFloats();
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, rgba_i);
	}
}