package xoric.prism.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AttachmentHeader implements IPackable
{
	private TextPacker textPacker;
	private IntPacker intPacker;

	private Text name;
	private boolean isCompressed;
	private int start;
	private int size;

	public AttachmentHeader()
	{
	}

	public AttachmentHeader(Text name, int start, int size, boolean isCompressed)
	{
		this.name = name;
		this.start = start;
		this.size = size;
		this.isCompressed = isCompressed;
	}

	private IntPacker getIntPacker()
	{
		if (intPacker == null)
			intPacker = new IntPacker();

		return intPacker;
	}

	private TextPacker getTextPacker()
	{
		if (textPacker == null)
			textPacker = new TextPacker();

		return textPacker;
	}

	public IText_r getName()
	{
		return name;
	}

	public int getStart()
	{
		return start;
	}

	public int getSize()
	{
		return size;
	}

	public boolean isCompressed()
	{
		return isCompressed;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// pack name
		getTextPacker().setText(name);
		textPacker.pack(stream);

		// pack compressed-flag
		int compressed = isCompressed ? 1 : 0;
		intPacker.setValue(compressed);
		intPacker.pack(stream);

		// pack start
		stream.write(start);
		stream.write(start >> 8);
		stream.write(start >> 16);
		stream.write(start >> 24);

		// pack size
		stream.write(size);
		stream.write(size >> 8);
		stream.write(size >> 16);
		stream.write(size >> 24);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		// unpack name
		getTextPacker().unpack(stream);
		name = textPacker.getText();

		// unpack compressed-flag
		intPacker.unpack(stream);
		int compressed = intPacker.getValue();
		isCompressed = compressed == 1;

		// unpack start
		start = stream.read();
		start |= stream.read() << 8;
		start |= stream.read() << 16;
		start |= stream.read() << 24;

		// unpack size
		size = stream.read();
		size |= stream.read() << 8;
		size |= stream.read() << 16;
		size |= stream.read() << 24;
	}

	@Override
	public int getPackedSize()
	{
		// name
		getTextPacker().setText(name);
		int size = textPacker.getPackedSize();

		// compressed-flag
		int compressed = isCompressed ? 1 : 0;
		intPacker.setValue(compressed);
		size += intPacker.getPackedSize();

		// start and size
		size += 4 + 4;

		return size;
	}
}
