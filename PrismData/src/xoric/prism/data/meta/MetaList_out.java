package xoric.prism.data.meta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.IPackable_out;
import xoric.prism.data.packable.IntPacker;

public class MetaList_out extends MetaListBase implements IPackable_out
{
	private final List<MetaBlock_out> blocks;

	public MetaList_out()
	{
		blocks = new ArrayList<MetaBlock_out>();
	}

	public MetaList_out(MetaList_in metaList)
	{
		blocks = new ArrayList<MetaBlock_out>(metaList.getBlockCount());

		for (int i = 0; i < metaList.getBlockCount(); ++i)
			blocks.add(new MetaBlock_out(metaList.getMetaBlock(i)));
	}

	public void addMetaBlock(MetaBlock_out b)
	{
		blocks.add(b);
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		// write block count
		IntPacker.pack_s(stream, blocks.size());

		// write blocks
		for (MetaBlock_out b : blocks)
			b.pack(stream);
	}

	@Override
	public int getBlockCount()
	{
		return blocks.size();
	}

	@Override
	protected MetaBlock_out getMetaBlock(int index)
	{
		return blocks.get(index);
	}

	@Override
	public void dropMetaBlock(MetaBlockBase b)
	{
		blocks.remove(b);
	}
}
