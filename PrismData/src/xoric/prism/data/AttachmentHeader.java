package xoric.prism.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AttachmentHeader implements IPackable
{
	private Text name;
	private boolean isCompressed;
	private int start;
	private int size;

	public AttachmentHeader()
	{
	}

	public AttachmentHeader(Text name, boolean isCompressed, int start, int size)
	{
		this.name = name;
		this.isCompressed = isCompressed;
		this.start = start;
		this.size = size;
	}

	public void setStart(int start)
	{
		this.start = start;
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
		TextPacker.pack_s(stream, name);

		// pack compressed-flag
		int compressed = isCompressed ? 1 : 0;
		IntPacker.pack_s(stream, compressed);

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
		name = TextPacker.unpack_s(stream);

		// unpack compressed-flag
		int compressed = IntPacker.unpack_s(stream);
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
		int size = TextPacker.getPackedSize_s(name);

		// compressed-flag
		int compressed = isCompressed ? 1 : 0;
		size += IntPacker.getPackedSize_s(compressed);

		// start and size
		size += 4 + 4;

		return size;
	}
}
