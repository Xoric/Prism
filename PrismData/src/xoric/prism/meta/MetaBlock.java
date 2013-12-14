package xoric.prism.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.IPackable;
import xoric.prism.data.IntPacker;

public class MetaBlock implements IPackable
{
	private MetaType metaType;
	private final List<MetaLine> list;
	private final IntPacker intPacker;

	public MetaBlock()
	{
		this.metaType = MetaType.COMMON;
		this.list = new ArrayList<MetaLine>();
		this.intPacker = new IntPacker();
	}

	public MetaBlock(MetaType metaType)
	{
		this.metaType = metaType;
		this.list = new ArrayList<MetaLine>();
		this.intPacker = new IntPacker();
	}

	public MetaType getToken()
	{
		return metaType;
	}

	public void addMetaLine(MetaLine metaLine)
	{
		list.add(metaLine);
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// write metaType
		intPacker.setValue(metaType.ordinal());
		intPacker.pack(stream);

		// write number of lines
		intPacker.setValue(list.size());
		intPacker.pack(stream);

		// write lines
		for (MetaLine l : list)
			l.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		// read metaType
		intPacker.unpack(stream);
		int value = intPacker.getValue();
		metaType = MetaType.values()[value]; // TODO: unsafe

		// read number of lines
		intPacker.unpack(stream);
		value = intPacker.getValue();

		// read lines
		for (int i = 0; i < value; ++i)
		{
			MetaLine l = new MetaLine();
			l.unpack(stream);
		}
	}

	@Override
	public int getPackedSize()
	{
		// metaType
		intPacker.setValue(metaType.ordinal());
		int size = intPacker.getPackedSize();

		// number of lines
		intPacker.setValue(list.size());
		size += intPacker.getPackedSize();

		// lines
		for (MetaLine l : list)
			size += l.getPackedSize();

		return size;
	}
}