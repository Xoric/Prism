package xoric.prism.data.meta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.IPackable_out;
import xoric.prism.data.packable.IntPacker;

public class MetaBlock_out extends MetaBlockBase implements IPackable_out
{
	protected final List<MetaLine_out> list;

	public MetaBlock_out(MetaType metaType, int version)
	{
		super(metaType, version);
		this.list = new ArrayList<MetaLine_out>();
	}

	public MetaBlock_out(MetaBlock_in mb)
	{
		super(mb.metaType, mb.version);
		this.list = new ArrayList<MetaLine_out>();

		for (MetaLine_in l : mb.getMetaLines())
			list.add(new MetaLine_out(l));
	}

	@Override
	public int getLineCount()
	{
		return list.size();
	}

	public void addMetaLine(MetaLine_out metaLine)
	{
		list.add(metaLine);
	}

	public void insertMetaLine(int index, MetaLine_out metaLine)
	{
		list.add(index, metaLine);
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		// write metaType and number of lines
		IntPacker.pack_s(stream, metaType.ordinal(), version, list.size());

		// write lines
		for (MetaLine_out l : list)
			l.pack(stream);
	}
}
