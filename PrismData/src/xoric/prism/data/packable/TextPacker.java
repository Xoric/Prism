package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.data.types.TextBuilder;

/**
 * @author XoricLee
 * @since 31.05.2011, 15:12:42
 */
public class TextPacker implements IPredictablePackable
{
	private static final TextPacker instance = new TextPacker();

	private static byte LOW = 0x3;
	private static byte MID = 0xC;
	private static byte HIGH = 0x30;

	private static byte LOW6 = 0x3F;
	private static int HIGH2INT = 0xC0;

	private final byte[] buf = new byte[4];

	private IText_r text;

	public void setText(IText_r text)
	{
		this.text = text;
	}

	public IText_r getText()
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
			byte c = text.symbolAt(i + 3);

			buf[0] = (byte) (text.symbolAt(i + 0) | ((c & LOW) << 6));
			buf[1] = (byte) (text.symbolAt(i + 1) | ((c & MID) << 4));
			buf[2] = (byte) (text.symbolAt(i + 2) | ((c & HIGH) << 2));

			stream.write(buf, 0, 3);

			i += 4;
		}
		for (int j = 0; j < rest; ++j)
			buf[j] = text.symbolAt(i++);
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
	public int calcPackedSize()
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

	public static synchronized void pack_s(OutputStream stream, IText_r text) throws IOException
	{
		instance.setText(text);
		instance.pack(stream);
	}

	public static synchronized Text unpack_s(InputStream stream) throws IOException
	{
		instance.unpack(stream);
		return new Text(instance.getText());
	}

	public static synchronized int calcPackedSize_s(IText_r text)
	{
		instance.setText(text);
		return instance.calcPackedSize();
	}
}
