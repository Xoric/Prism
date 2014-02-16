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

public class MetaBlock_in extends MetaBlockBase implements IPackable_in, IInfoLayer
{
	private final List<MetaLine_in> list;

	private IInfoLayer uplink;

	public MetaBlock_in()
	{
		this.metaType = MetaType.COMMON;
		this.list = new ArrayList<MetaLine_in>();
	}

	public MetaBlock_in(MetaType metaType, int version)
	{
		super(metaType, version);
		this.list = new ArrayList<MetaLine_in>();
	}

	public MetaBlock_in(MetaBlock_out m)
	{
		super(m.metaType, m.version);
		this.list = new ArrayList<MetaLine_in>(m.getLineCount());

		for (MetaLine_out l : m.list)
			list.add(new MetaLine_in(this, l));
	}

	public void addMetaLine(MetaLine_in ml)
	{
		list.add(ml);
	}

	@Override
	public int getLineCount()
	{
		return list.size();
	}

	public int findNextIndex(MetaKey key, int startIndex)
	{
		for (int i = startIndex; i < list.size(); ++i)
			if (list.get(i).getKey() == key)
				return i;

		return -1;
	}

	public MetaLine_in getMetaLine(int index)
	{
		return list.get(index);
	}

	public List<MetaLine_in> getMetaLines()
	{
		return list;
	}

	public MetaLine_in claimLine(MetaKey key) throws PrismException
	{
		for (MetaLine_in l : list)
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

	public List<MetaLine_in> findLines(MetaKey key)
	{
		List<MetaLine_in> list = new ArrayList<MetaLine_in>();

		for (MetaLine_in l : this.list)
			if (l.getKey() == key)
				list.add(l);

		return list;
	}

	public int countLines(MetaKey key)
	{
		int n = 0;

		for (MetaLine_in l : list)
			if (l.getKey() == key)
				++n;

		return n;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		// read metaType
		int value = IntPacker.unpack_s(stream);
		metaType = MetaType.valueOf(value);

		// read number of lines
		value = IntPacker.unpack_s(stream);

		// read lines
		for (int i = 0; i < value; ++i)
		{
			MetaLine_in l = new MetaLine_in(this);
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

		e.code.addInfo(MetaType.class.getSimpleName(), metaType.toString());
	}
}
