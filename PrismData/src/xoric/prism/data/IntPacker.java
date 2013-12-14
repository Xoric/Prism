package xoric.prism.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IntPacker implements IPackable
{
	private static final byte buf[] = new byte[4];
	private static final int capacities[] = { 64, 16384, 4194304, 1073741824 };

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
		int bytes = getPackedSize();
		int packed = bytes - 1;
		packed |= value << 2;

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

		value = k >> 2;
		for (int i = 1; i < bytes; ++i)
			value |= (0xFF & buf[i]) << (i * 8 - 2);
	}

	@Override
	public int getPackedSize()
	{
		int v = value;
		for (int i = 0; i < 4; ++i)
		{
			int cap = capacities[i];
			if (v < cap)
				return i + 1;
		}
		return 4;
	}
}