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
	private int localFileVersion;
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

	public MetaBlock getMetaBlock(MetaType t)
	{
		for (MetaBlock b : blocks)
			if (b.getToken() == t)
				return b;

		return null;
	}

	public boolean hasMetaBlock(MetaType t)
	{
		return getMetaBlock(t) != null;
	}

	public int getLocalFileVersion()
	{
		return localFileVersion;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// write localFileVersion
		intPacker.setValue(localFileVersion);
		intPacker.pack(stream);

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
		// read localFileVersion
		intPacker.unpack(stream);
		localFileVersion = intPacker.getValue();

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
		// localFileVersion
		intPacker.setValue(localFileVersion);
		int size = intPacker.getPackedSize();

		// block count
		intPacker.setValue(blocks.size());
		size += intPacker.getPackedSize();

		// blocks
		for (MetaBlock b : blocks)
			size += b.getPackedSize();

		return size;
	}
}
