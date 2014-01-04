package xoric.prism.data.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FloatPacker implements IPackable
{
	private static final FloatPacker instance = new FloatPacker();

	private static final byte buf[] = new byte[4];
	private static final int capacities[] = { 64 - 1, 16384 - 1, 4194304 - 1, 1073741824 - 1, 4 - 1, 1024 - 1, 262144 - 1, 67108864 - 1, 0,
			128 - 1, 32768 - 1, 8388608 - 1 };
	private static final int divisors[] = { 10, 100, 1000 };
	private static final int shiftAmounts[] = { 2, 6, 9 };
	private static final float factors[] = { 10, 100, 1000 };

	private float value;
	private int decimalPlaces;
	private int entry;

	public void setValue(float value)
	{
		this.value = value;
	}

	public float getValue()
	{
		return value;
	}

	/**
	 * Sets the number of decimal places.
	 * @param decimalPlaces
	 *            number of decimal places [0-2]
	 */
	public void setDecimalPlaces(int decimalPlaces)
	{
		this.decimalPlaces = decimalPlaces;
		this.entry = decimalPlaces * 4;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		int bytes = getPackedSize();
		int packed = bytes - 1;
		long temp = ((long) (value * factors[decimalPlaces])) % divisors[decimalPlaces];
		int carry = ((temp % 10) >= 5) ? 1 : 0;
		int preComma = (int) value;

		if (decimalPlaces == 0)
		{
			preComma += carry;
		}
		else
		{
			final int cap = decimalPlaces == 1 ? 10 : 100;
			int postComma = (int) (carry + temp / 10);

			/*
			if (postComma >= cap)
				postComma = cap - 1;
			*/
			if (postComma >= cap)
			{
				postComma = 0;
				++preComma;
			}
			packed |= postComma << 2;
		}
		packed |= preComma << shiftAmounts[decimalPlaces];

		buf[0] = (byte) packed;
		for (int i = 1; i < bytes; ++i)
			buf[i] = (byte) (packed >> (8 * i));

		stream.write(buf, 0, bytes);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		int k = stream.read() & 0xFF;
		int bytes = (k & 0x3) + 1;
		int preComma;
		value = 0.0f;
		stream.read(buf, 1, bytes - 1);

		if (decimalPlaces == 0)
		{
			preComma = (k >> 2) & 0xFF;
			for (int i = 1; i < bytes; ++i)
				preComma |= buf[i] << (i * 8 - 2);

			value = 0.0f;
		}
		else
		{
			int postComma;
			if (decimalPlaces == 1)
			{
				postComma = (k >> 2) & 0xF;
				preComma = (k >> 6) & 0xFF;
				for (int i = 1; i < bytes; ++i)
					preComma |= (buf[i] & 0xFF) << (i * 8 - 6);

				value = 0.1f * postComma;
			}
			else
			{
				postComma = (k >> 2) & 0xFF;
				postComma |= (buf[1] & 0x1) << 6;
				preComma = (buf[1] & 0xFF) >> 1;
				for (int i = 2; i < bytes; ++i)
					preComma |= (buf[i] & 0xFF) << (i * 8 - 9);

				value = 0.01f * postComma;
			}
		}
		value += preComma;
	}

	@Override
	public int getPackedSize()
	{
		int v = (int) value;
		for (int i = 0; i < 4; ++i)
		{
			int cap = capacities[entry + i];
			if (v < cap)
				return i + 1;
		}
		return 4;
	}

	public static synchronized void pack_s(OutputStream stream, float value, int decimalPlaces) throws IOException
	{
		instance.setDecimalPlaces(decimalPlaces);
		instance.setValue(value);
		instance.pack(stream);
	}

	public static synchronized float unpack_s(InputStream stream, int decimalPlaces) throws IOException
	{
		instance.setDecimalPlaces(decimalPlaces);
		instance.unpack(stream);
		return instance.getValue();
	}

	public static synchronized int getPackedSize_s(float value, int decimalPlaces)
	{
		instance.setDecimalPlaces(decimalPlaces);
		instance.setValue(value);
		return instance.getPackedSize();
	}
}