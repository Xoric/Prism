package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

public class MetaBlock implements IPackable, IInfoLayer
{
	private MetaType metaType;
	private int version;
	private final List<MetaLine> list;

	private IInfoLayer uplink;

	public MetaBlock()
	{
		this.metaType = MetaType.COMMON;
		this.list = new ArrayList<MetaLine>();
	}

	public MetaBlock(MetaType metaType, int version)
	{
		this.metaType = metaType;
		this.version = version;
		this.list = new ArrayList<MetaLine>();
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
		metaLine.setUplink(this);
	}

	public void insertMetaLine(int index, MetaLine metaLine)
	{
		this.list.add(index, metaLine);
		metaLine.setUplink(this);
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

	public MetaLine claimLine(MetaKey key) throws PrismException
	{
		for (MetaLine l : list)
			if (l.getKey() == key)
				return l;

		PrismException e = new PrismException();
		// ----
		e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
		// ----
		e.code.setText("non-existing MetaLine requested");
		addExceptionInfoTo(e);
		e.code.addInfo("missing MetaLine", key.toString());
		// ----
		throw e;
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
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		// write metaType and number of lines
		IntPacker.pack_s(stream, metaType.ordinal());
		IntPacker.pack_s(stream, list.size());

		// write lines
		for (MetaLine l : list)
			l.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		// read metaType
		int value = IntPacker.unpack_s(stream);
		metaType = MetaType.values()[value]; // TODO: unsafe

		// read number of lines
		value = IntPacker.unpack_s(stream);

		// read lines
		for (int i = 0; i < value; ++i)
		{
			MetaLine l = new MetaLine();
			l.setUplink(this);
			l.unpack(stream);
			list.add(l);
		}
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

		e.code.addInfo("MetaBlock", metaType.toString());
	}
}
