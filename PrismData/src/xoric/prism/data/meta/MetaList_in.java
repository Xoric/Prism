package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.packable.IPackable_in;
import xoric.prism.data.packable.IntPacker;

public class MetaList_in extends MetaListBase implements IPackable_in, IInfoLayer
{
	private final List<MetaBlock_in> blocks;

	private IInfoLayer uplink;

	public MetaList_in()
	{
		blocks = new ArrayList<MetaBlock_in>();
	}

	public MetaList_in(MetaList_out m)
	{
		blocks = new ArrayList<MetaBlock_in>(m.getBlockCount());

		for (int i = 0; i < m.getBlockCount(); ++i)
			blocks.add(new MetaBlock_in(m.getMetaBlock(i)));
	}

	public void addMetaBlock(MetaBlock_in b)
	{
		b.setUplink(uplink);
		blocks.add(b);
	}

	public MetaBlock_in claimMetaBlock(MetaType t) throws PrismException
	{
		for (MetaBlock_in b : blocks)
			if (b.getMetaType() == t)
				return b;

		PrismException e = new PrismException();
		// ----
		e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
		// ----
		e.code.setText("requested MetaBlock does not exist");
		e.code.addInfo("MetaType", t.toString());
		// ----
		addExceptionInfoTo(e);
		// ----
		throw e;
	}

	@Override
	public MetaBlock_in getMetaBlock(int index)
	{
		return blocks.get(index);
	}

	public boolean hasMetaBlock(MetaType t)
	{
		for (MetaBlock_in b : blocks)
			if (b.getMetaType() == t)
				return true;

		return false;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		// read block count
		int n = IntPacker.unpack_s(stream);

		// read blocks
		for (int i = 0; i < n; ++i)
		{
			MetaBlock_in b = new MetaBlock_in();
			b.unpack(stream);
			addMetaBlock(b);
		}
	}

	@Override
	public int getBlockCount()
	{
		return blocks.size();
	}

	public IInfoLayer getParent()
	{
		return uplink;
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

	@Override
	public void dropMetaBlock(MetaBlockBase b)
	{
		blocks.remove(b);
	}
}
