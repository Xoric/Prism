package xoric.prism.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AttachmentHeader implements IPackable
{
	private TextPacker textPacker;
	private IntPacker intPacker;

	private Text name;
	private final int start;
	private int size;
	private boolean isCompressed;

	public AttachmentHeader(int start)
	{
		this.start = start;
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
		getTextPacker().setText(name);
		textPacker.pack(stream);

		getIntPacker().setValue(this.size);
		intPacker.pack(stream);

		int compressed = isCompressed ? 1 : 0;
		intPacker.setValue(compressed);
		intPacker.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		getTextPacker().unpack(stream);
		name = textPacker.getText();

		getIntPacker().unpack(stream);
		size = intPacker.getValue();

		intPacker.unpack(stream);
		int compressed = intPacker.getValue();
		isCompressed = compressed == 1;
	}

	@Override
	public int getPackedSize()
	{
		getTextPacker().setText(name);
		int size = textPacker.getPackedSize();

		getIntPacker().setValue(this.size);
		size += intPacker.getPackedSize();

		int compressed = isCompressed ? 1 : 0;
		intPacker.setValue(compressed);
		size += intPacker.getPackedSize();

		return size;
	}
}