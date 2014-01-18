package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IntPacker;

public class MetaList implements IPackable, IInfoLayer
{
	private final List<MetaBlock> blocks;
	private final IntPacker intPacker;

	private IInfoLayer uplink;

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

	public void addMetaBlock(MetaBlock b)
	{
		b.setUplink(uplink);
		blocks.add(b);
	}

	public MetaBlock claimMetaBlock(MetaType t) throws PrismException
	{
		for (MetaBlock b : blocks)
			if (b.getMetaType() == t)
				return b;

		PrismException e = new PrismException();
		// ----
		e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
		// ----
		e.code.setText("non-existing MetaBlock requested");
		addExceptionInfoTo(e);
		e.code.addInfo("MetaType", t.toString());
		// ----
		addExceptionInfoTo(e);
		// ----
		throw e;
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
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		// read block count
		intPacker.unpack(stream);
		int n = intPacker.getValue();

		// read blocks
		for (int i = 0; i < n; ++i)
		{
			MetaBlock mb = new MetaBlock();
			mb.unpack(stream);
			addMetaBlock(mb);
		}
	}

	public int getBlockCount()
	{
		return blocks.size();
	}

	@Override
	public void setUplink(IInfoLayer uplink)
	{
		this.uplink = uplink;
	}

	@Override
	public void addExceptionInfoTo(PrismException e)
	{
		if (uplink != null)
			uplink.addExceptionInfoTo(e);
		else
			e.code.addInfo("MetaFile", "null");
	}

	public void dropMetaBlock(MetaBlock devBlock)
	{
		blocks.remove(devBlock);
	}
}
