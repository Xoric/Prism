package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.PrismMetaFileException;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IntPacker;

public class MetaList implements IPackable, IActor
{
	private final List<MetaBlock> blocks;
	private final IntPacker intPacker;
	private IMetaListOwner owner;

	public MetaList()
	{
		blocks = new ArrayList<MetaBlock>();
		intPacker = new IntPacker();
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[");

		for (int i = 0; i < blocks.size(); ++i)
		{
			MetaBlock b = blocks.get(i);
			if (i > 0)
				sb.append(", ");

			sb.append(b.getMetaType().toString());
		}
		sb.append("]");

		return sb.toString();
	}

	public void setOwner(IMetaListOwner owner)
	{
		this.owner = owner;
	}

	public void addMetaBlock(MetaBlock b)
	{
		blocks.add(b);
	}

	public MetaBlock findMetaBlock(MetaType t) throws PrismException
	{
		for (MetaBlock b : blocks)
			if (b.getMetaType() == t)
				return b;

		ErrorCode c = new ErrorCode(this, ErrorID.META_BLOCK_MISSING);
		if (owner != null)
		{
			PrismMetaFileException e = new PrismMetaFileException(c, owner.getMetaFilename());
			e.appendInfo("block", t.toString());
			throw e;
		}
		else
		{
			PrismException e = new PrismException(c);
			e.appendInfo("block", t.toString());
			throw e;
		}
	}

	public MetaBlock getMetaBlock(int index)
	{
		return blocks.get(index);
	}

	public boolean hasMetaBlock(MetaType t)
	{
		for (MetaBlock b : blocks)
			if (b.getMetaType() == t)
				return true;

		return false;
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
			blocks.add(b);
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

	@Override
	public ActorID getActorID()
	{
		return ActorID.META_LIST;
	}
}