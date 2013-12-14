package xoric.prism.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Felix M�hrle
 * @since 31.05.2011, 15:12:42
 */
public class TextPacker implements IPackable
{
	private static byte LOW = 0x3;
	private static byte MID = 0xC;
	private static byte HIGH = 0x30;

	private static byte LOW6 = 0x3F;
	private static int HIGH2INT = 0xC0;

	private final byte[] buf = new byte[4];

	private Text text;

	public void setText(Text text)
	{
		this.text = text;
	}

	public Text getText()
	{
		return text;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// pack length
		boolean isExtended = isExtended();
		int length = text.length();
		int k = isExtended ? 1 : 0;
		k |= length << 1;
		buf[0] = (byte) k;

		int lenBytes = isExtended ? 2 : 1;
		if (isExtended)
			buf[1] = (byte) (length >> 7);

		stream.write(buf, 0, lenBytes);

		// pack data
		int rest = length % 4;
		int full = length - rest;

		int i = 0;
		while (i < full)
		{
			byte c = text.indexAt(i + 3);

			buf[0] = (byte) (text.indexAt(i + 0) | ((c & LOW) << 6));
			buf[1] = (byte) (text.indexAt(i + 1) | ((c & MID) << 4));
			buf[2] = (byte) (text.indexAt(i + 2) | ((c & HIGH) << 2));

			stream.write(buf, 0, 3);

			i += 4;
		}
		for (int j = 0; j < rest; ++j)
			buf[j] = text.indexAt(i++);
		stream.write(buf, 0, rest);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		// unpack length
		int k = 0xFF & stream.read();
		boolean isExtended = (k & 0x1) > 0;
		int length = k >> 1;

		if (isExtended)
		{
			k = 0xFF & stream.read();
			length |= k << 7;
		}

		// unpack data
		TextBuilder tb = new TextBuilder();
		text = tb;

		int rest = length % 4;
		int full = length - rest;

		tb.setLength(length);

		int i = 0;
		while (i < full)
		{
			stream.read(buf, 0, 3);

			buf[3] = (byte) (((buf[0] & HIGH2INT) >> 6) | ((buf[1] & HIGH2INT) >> 4) | ((buf[2] & HIGH2INT) >> 2));
			buf[0] &= LOW6;
			buf[1] &= LOW6;
			buf[2] &= LOW6;

			tb.setSymbols(i, buf, 4);

			i += 4;
		}
		stream.read(buf, 0, rest);
		tb.setSymbols(i, buf, rest);
	}

	@Override
	public int getPackedSize()
	{
		int bytes = isExtended() ? 2 : 1;
		int length = text.length();
		int rest = length % 4;
		int full = length - rest;

		bytes += (full >> 2) * 3 + rest;

		return bytes;
	}

	private boolean isExtended()
	{
		return text.length() >= 128;
	}
}