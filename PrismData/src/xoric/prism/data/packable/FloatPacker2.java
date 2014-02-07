package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
public class FloatPacker2
{
	private static final byte buf[] = new byte[4];
	private static final int capacities[] = {/*dp0*/32, 8192, 2097152, 134217728,/*dp1*/2, 512, 131072, 8388608,/*dp2*/0, 64, 16384,
			1048576 };
	private static final int divisors[] = { 10, 100, 1000 };
	private static final int shiftAmounts[] = { 3, 7, 10 };
	private static final float factors[] = { 10, 100, 1000 };

	public static synchronized void pack_s(OutputStream stream, float value, int decimalPlaces) throws IOException
	{
		int bytes = calcPackedSize(value, decimalPlaces);
		int sign = value >= 0.0f ? 0 : 1;
		value = Math.abs(value);

		int packed = (bytes - 1) | (sign << 2);

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

			if (postComma >= cap)
			{
				postComma = 0;
				++preComma;
			}
			packed |= postComma << 3;
		}
		int ii = preComma << 3;
		packed |= preComma << shiftAmounts[decimalPlaces];

		buf[0] = (byte) packed;
		for (int i = 1; i < bytes; ++i)
			buf[i] = (byte) (packed >> (8 * i));

		stream.write(buf, 0, bytes);
	}

	public static synchronized float unpack_s(InputStream stream, int decimalPlaces) throws IOException
	{
		int k = stream.read() & 0xFF; // lowest byte
		int bytes = (k & 0x3) + 1;
		int sign = (k >> 2) & 0x1;

		if (bytes > 1)
			stream.read(buf, 1, bytes - 1);

		int packed = 0;
		for (int i = 0; i < bytes; ++i)
			packed |= (buf[i] & 0xFF /*required*/) << (8 * i);

		float value;

		if (decimalPlaces == 0)
		{
			int preComma = packed >> 3;
			value = preComma;
		}
		else
		{
			if (decimalPlaces == 1)
			{
				int postComma = (packed >> 3) & 0xF;
				int preComma = packed >> 7;
				value = preComma + 0.1f * postComma;
			}
			else
			{
				int postComma = (packed >> 3) & 0x7F;
				int preComma = packed >> 10;
				value = preComma + 0.01f * postComma;
			}
		}

		if (sign > 0)
			value = -value;

		return value;
	}

	public static int calcPackedSize(float value, int decimalPlaces)
	{
		int v = Math.abs((int) value);
		int entry = decimalPlaces * 4;

		for (int i = 0; i < 3; ++i)
		{
			int cap = capacities[entry + i];
			if (v < cap)
				return i + 1;
		}
		return 4;
	}

	/*
	public static void test(int dp) throws IOException
	{
		final int entry = dp * 4;
		int count = capacities[entry + 3];
		int max = count - 1;
		int pdmax;
		if (dp == 1)
			pdmax = 10;
		else if (dp == 2)
			pdmax = 100;
		else
			pdmax = 1;

		float pdfactor;
		if (dp == 1)
			pdfactor = 0.1f;
		else if (dp == 2)
			pdfactor = 0.01f;
		else
			pdfactor = 0.0f;

		int f = 0;
		int s = 0;

		//		final float tolerance = 0.01f;
		final float tolerance = 0.0001f;

		//		for (int predecimals = -max; predecimals < max; ++predecimals)
		for (int predecimals = -10000; predecimals < 10000; ++predecimals)
		{
			for (int postdecimals = 0; postdecimals < pdmax; ++postdecimals)
			{
				float f1 = predecimals + pdfactor * postdecimals;

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				pack_s(stream, f1, dp);
				stream.close();
				byte[] buf = stream.toByteArray();

				ByteArrayInputStream stream2 = new ByteArrayInputStream(buf);
				float f2 = unpack_s(stream2, dp);
				stream2.close();

				if (f1 >= f2 - tolerance && f1 <= f2 + tolerance)
				{
					s++;
				}
				else
				{
					f++;
					System.out.println(f1 + " ... " + f2 + " (dp" + dp + ")");
				}
			}
		}
		System.out.println("dp" + dp);
		System.out.println("  successful: " + s);
		System.out.println("  failed: " + f);
	}

	public static void main(String[] args)
	{
		try
		{
			int dp = 2;

			test(dp);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	*/
}
