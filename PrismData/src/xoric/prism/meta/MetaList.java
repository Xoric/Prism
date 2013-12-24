package xoric.prism.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.IPackable;
import xoric.prism.data.IntPacker;

public class MetaList implements IPackable
{
	private final List<MetaBlock> blocks;
	private final IntPacker intPacker;

	public MetaList()
	{
		blocks = new ArrayList<MetaBlock>();
		intPacker = new IntPacker();
	}

	public void addMetaBlock(MetaBlock b)
	{
		blocks.add(b);
	}

	public MetaBlock findMetaBlock(MetaType t)
	{
		for (MetaBlock b : blocks)
			if (b.getMetaType() == t)
				return b;

		return null;
	}

	public MetaBlock getMetaBlock(int index)
	{
		return blocks.get(index);
	}

	public boolean hasMetaBlock(MetaType t)
	{
		return findMetaBlock(t) != null;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// write block count
		intPacker.setValue(blocks.size());
		intPacker.pack(stream);

		// write blocks
		for (MetaBlock b : blocks)
			b.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		// read block count
		intPacker.unpack(stream);
		int n = intPacker.getValue();

		// read blocks
		for (int i = 0; i < n; ++i)
		{
			MetaBlock b = new MetaBlock();
			b.unpack(stream);
		}
	}

	@Override
	public int getPackedSize()
	{
		// block count
		intPacker.setValue(blocks.size());
		int size = intPacker.getPackedSize();

		// blocks
		for (MetaBlock b : blocks)
			size += b.getPackedSize();

		return size;
	}

	public int getBlockCount()
	{
		return blocks.size();
	}
}
