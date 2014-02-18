package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public class FloatPacker3
{
	private static final byte buf[] = new byte[4];
	private static final int capacities[] = {/*dp1*/2, 512, 131072, 8388608,/*dp2*/0, 64, 16384, 1048576 };

	public static synchronized void pack_s(OutputStream stream, List<Float> floats, int decimalPlaces) throws PrismException, IOException
	{
		for (float f : floats)
			pack_s(stream, f, decimalPlaces);
	}

	/**
	 * Packs a float with either 1 or 2 decimal places into an OutputStream.
	 * @param stream
	 * @param value
	 *            float to be packed
	 * @param decimalPlaces
	 *            number of decimal places (1 or 2)
	 * @throws IOException
	 * @throws PrismException
	 */
	public static synchronized void pack_s(OutputStream stream, float value, int decimalPlaces) throws PrismException, IOException
	{
		checkDecimalPlaces(decimalPlaces);

		int bytes = calcPackedSizeUnsafe(value, decimalPlaces);
		int sign = value >= 0.0f ? 0 : 1;
		value = Math.abs(value);

		int packed = (bytes - 1) | (sign << 2);

		long temp;
		if (decimalPlaces == 1)
			temp = (long) ((value * 100.0f) % 100);
		else
			temp = (long) ((value * 1000.0f) % 1000);

		int carry = ((temp % 10) >= 5) ? 1 : 0;
		int preComma = (int) value;

		int entry = (decimalPlaces - 1) * 4;
		int max = capacities[entry + 3] - 1;
		if (preComma > max)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("float value too big");
			e.code.addInfo("predecimal value", preComma);
			e.code.addInfo("maximum value", max);
			e.code.addInfo("decimal places", decimalPlaces);
			throw e;
		}

		final int cap = decimalPlaces == 1 ? 10 : 100;
		int postComma = (int) (carry + temp / 10);

		if (postComma >= cap)
		{
			postComma = 0;
			++preComma;
		}

		packed |= postComma << 3;
		packed |= preComma << (decimalPlaces == 1 ? 7 : 10);

		buf[0] = (byte) packed;
		for (int i = 1; i < bytes; ++i)
			buf[i] = (byte) (packed >> (8 * i));

		stream.write(buf, 0, bytes);
	}

	public static synchronized void unpack_s(InputStream stream, List<Float> floats, int decimalPlaces, int n) throws IOException,
			PrismException
	{
		for (int i = 0; i < n; ++i)
			floats.add(unpack_s(stream, decimalPlaces));
	}

	public static synchronized float unpack_s(InputStream stream, int decimalPlaces) throws IOException, PrismException
	{
		checkDecimalPlaces(decimalPlaces);

		int k = stream.read() & 0xFF; // lowest byte
		int bytes = (k & 0x3) + 1;
		int sign = (k >> 2) & 0x1;

		if (bytes > 1)
			stream.read(buf, 1, bytes - 1);

		int packed = k;
		for (int i = 0; i < bytes; ++i)
			packed |= (buf[i] & 0xFF /*required*/) << (8 * i);

		float value;

		if (decimalPlaces == 1)
		{
			int postComma = (packed >> 3) & 0xF;
			int preComma = packed >> 7;
			value = preComma + 0.1f * postComma;
		}
		else
		// decimalPlaces == 2
		{
			int postComma = (packed >> 3) & 0x7F;
			int preComma = packed >> 10;
			value = preComma + 0.01f * postComma;
		}

		if (sign > 0)
			value = -value;

		return value;
	}

	public static int calcPackedSize(float value, int decimalPlaces) throws PrismException
	{
		checkDecimalPlaces(decimalPlaces);
		return calcPackedSizeUnsafe(value, decimalPlaces);
	}

	private static int calcPackedSizeUnsafe(float value, int decimalPlaces)
	{
		int v = Math.abs((int) value);
		int entry = (decimalPlaces - 1) * 4;

		for (int i = 0; i < 3; ++i)
		{
			int cap = capacities[entry + i];
			if (v < cap)
				return i + 1;
		}
		return 4;
	}

	private static void checkDecimalPlaces(int decimalPlaces) throws PrismException
	{
		if (decimalPlaces < 1 || decimalPlaces > 2)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("invalid number of decimal places in FloatPacker");
			e.code.addInfo("decimal places", decimalPlaces);
			throw e;
		}
	}

	public static int calcPackedSize(List<Float> floats, int decimalPlaces) throws PrismException
	{
		checkDecimalPlaces(decimalPlaces);

		int bytes = 0;

		for (Float f : floats)
			bytes += calcPackedSizeUnsafe(f, decimalPlaces);

		return bytes;
	}
}
