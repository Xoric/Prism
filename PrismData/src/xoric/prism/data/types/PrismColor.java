package xoric.prism.data.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

public class PrismColor implements IPackable
{
	public static PrismColor opaqueWhite = new PrismColor(255, 255, 255, 255);
	public static PrismColor temp = new PrismColor(255, 0, 50, 255);

	private final float[] rgba_f;
	private final int[] rgba_i;

	public PrismColor()
	{
		rgba_f = new float[4];
		rgba_i = new int[4];
	}

	public PrismColor(int r, int g, int b, int a)
	{
		rgba_f = new float[4];
		rgba_i = new int[4];
		set(r, g, b, a);
	}

	public PrismColor(float r, float g, float b, float a)
	{
		rgba_f = new float[4];
		rgba_i = new int[4];
		set(r, g, b, a);
	}

	@Override
	public String toString()
	{
		return "r=" + rgba_i[0] + ", g=" + rgba_i[1] + ", b=" + rgba_i[2] + ", a=" + rgba_i[3];
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

	public void setAlpha(int a)
	{
		rgba_i[3] = a;
		rgba_f[3] = rgba_i[3] / 255.0f;
	}

	public void setAlpha(float a)
	{
		rgba_f[3] = a;
		rgba_i[3] = (int) (255 * a);
	}

	public int getAlpha()
	{
		return rgba_i[3];
	}

	public float getComponent_f(int i)
	{
		return rgba_f[i];
	}

	public float getComponent_i(int i)
	{
		return rgba_i[i];
	}

	public void setComponent(int i, int v)
	{
		rgba_i[i] = v;
		rgba_f[i] = rgba_i[i] / 255.0f;
	}

	public void setComponent(int i, float f)
	{
		rgba_f[i] = f;
		rgba_i[i] = (int) (255 * f);
	}

	public void set(PrismColor color)
	{
		for (int i = 0; i < 4; ++i)
		{
			rgba_f[i] = color.rgba_f[i];
			rgba_i[i] = color.rgba_i[i];
		}
	}
}
