package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.Heap;
import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IntPacker;

public class MetaBlock implements IPackable
{
	private MetaType metaType;
	private int version;
	private final List<MetaLine> list;
	private final IntPacker intPacker;

	public MetaBlock()
	{
		this.metaType = MetaType.COMMON;
		this.list = new ArrayList<MetaLine>();
		this.intPacker = new IntPacker();
	}

	public MetaBlock(MetaType metaType, int version)
	{
		this.metaType = metaType;
		this.version = version;
		this.list = new ArrayList<MetaLine>();
		this.intPacker = new IntPacker();
	}

	@Override
	public String toString()
	{
		return list.size() + " line(s)";
	}

	public int getLineCount()
	{
		return list.size();
	}

	public MetaType getMetaType()
	{
		return metaType;
	}

	public int getVersion()
	{
		return version;
	}

	public void addMetaLine(MetaLine metaLine)
	{
		this.list.add(metaLine);
	}

	public int findNextIndex(MetaKey key, int startIndex)
	{
		for (int i = startIndex; i < list.size(); ++i)
			if (list.get(i).getKey() == key)
				return i;

		return -1;
	}

	public MetaLine getMetaLine(int index)
	{
		return list.get(index);
	}

	public List<MetaLine> getMetaLines()
	{
		return list;
	}

	public Heap findKey(MetaKey key)
	{
		for (MetaLine l : list)
			if (l.getKey() == key)
				return l.getHeap();

		return null;
	}

	public List<MetaLine> findLines(MetaKey key)
	{
		List<MetaLine> list = new ArrayList<MetaLine>();

		for (MetaLine l : this.list)
			if (l.getKey() == key)
				list.add(l);

		return list;
	}

	public int countLines(MetaKey key)
	{
		int n = 0;

		for (MetaLine l : list)
			if (l.getKey() == key)
				++n;

		return n;
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
			list.add(l);
		}
	}

	//	@Override
	//	public int getPackedSize()
	//	{
	//		// metaType
	//		intPacker.setValue(metaType.ordinal());
	//		int size = intPacker.getPackedSize();
	//
	//		// number of lines
	//		intPacker.setValue(list.size());
	//		size += intPacker.getPackedSize();
	//
	//		// lines
	//		for (MetaLine l : list)
	//			size += l.getPackedSize();
	//
	//		return size;
	//	}
}
