package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class IntPacker implements IPredictablePackable
{
	// TODO reimplement as abstract class without members and only static methods
	private static final IntPacker instance = new IntPacker();

	private static final byte buf[] = new byte[4];
	//	private static final int capacities[] = { 64, 16384, 4194304, 1073741824 };
	private static final int capacities[] = { 32, 8192, 2097152, 536870912 };

	private int value;

	public void setValue(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		int bytes = calcPackedSize();
		int packed = bytes - 1;
		//		packed |= value << 2;
		int sign = value >= 0 ? 0 : 1;
		packed |= sign << 2;
		packed |= Math.abs(value) << 3;

		for (int i = 0; i < bytes; ++i)
			buf[i] = (byte) (packed >> (8 * i));

		stream.write(buf, 0, bytes);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		int k = 0xFF & stream.read();
		int bytes = 1 + (k & 0x3);
		stream.read(buf, 1, bytes - 1);

		//		value = k >> 2;
		int sign = (k >> 2) & 0x1;
		value = k >> 3;

		for (int i = 1; i < bytes; ++i)
			value |= (0xFF & buf[i]) << (i * 8 - 3);
		//		value |= (0xFF & buf[i]) << (i * 8 - 2);

		if (sign > 0)
			value = -value;
	}

	@Override
	public int calcPackedSize()
	{
		//		int v = value;
		int v = Math.abs(value);

		for (int i = 0; i < 4; ++i)
		{
			int cap = capacities[i];
			if (v < cap)
				return i + 1;
		}
		return 4;
	}

	/**
	 * Packs a given integer into an OutputStream. Returns the number of bytes written.
	 * @param stream
	 * @param value
	 * @return int
	 * @throws IOException
	 */
	public static synchronized void pack_s(OutputStream stream, int value) throws IOException
	{
		instance.setValue(value);
		instance.pack(stream);
	}

	public static synchronized void pack_s(OutputStream stream, int... values) throws IOException
	{
		for (int v : values)
		{
			instance.setValue(v);
			instance.pack(stream);
		}
	}

	public static synchronized void pack_s(OutputStream stream, List<Integer> values) throws IOException
	{
		for (int v : values)
		{
			instance.setValue(v);
			instance.pack(stream);
		}
	}

	public static synchronized int unpack_s(InputStream stream) throws IOException
	{
		instance.unpack(stream);
		return instance.getValue();
	}

	public static synchronized void unpack_s(InputStream stream, int[] out) throws IOException
	{
		for (int i = 0; i < out.length; ++i)
		{
			instance.unpack(stream);
			out[i] = instance.getValue();
		}
	}

	public static void unpack_s(InputStream stream, final int n, List<Integer> intSources) throws IOException
	{
		for (int i = 0; i < n; ++i)
		{
			instance.unpack(stream);
			intSources.add(instance.getValue());
		}
	}

	public static synchronized int calcPackedSize_s(int value)
	{
		instance.setValue(value);
		return instance.calcPackedSize();
	}

	/*
	public static void main(String[] args)
	{
		try
		{
			int f = 0;
			int s = 0;

			for (int v1 = -536870911; v1 < 536870912; ++v1)
			{
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				IntPacker.pack_s(stream, v1);
				stream.close();
				byte[] buf = stream.toByteArray();

				ByteArrayInputStream stream2 = new ByteArrayInputStream(buf);
				int v2 = IntPacker.unpack_s(stream2);
				stream2.close();

				if (v1 == v2)
				{
					++s;
				}
				else
				{
					++f;
					System.out.println(v1 + " ... " + v2);
				}
			}
			System.out.println("successful: " + s);
			System.out.println("failed: " + f);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	*/
}
