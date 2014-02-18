package xoric.prism.data.types;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;

public class BooleanList
{
	private final List<Booleans> list;
	private int nextIndex;

	public BooleanList()
	{
		list = new ArrayList<Booleans>();
	}

	public void add(boolean b)
	{
		if (list.size() == 0 || nextIndex % 8 == 0)
			list.add(new Booleans());

		Booleans c = list.get(list.size() - 1);
		c.setValue(nextIndex, b);

		nextIndex = (++nextIndex) % 8;
	}

	public void set(int index, boolean b) throws PrismException
	{
		final int n = index / 8;

		while (list.size() < n)
			list.add(new Booleans());

		list.get(index / 8).setValue(index % 8, b);
	}

	public void clear()
	{
		list.clear();
		nextIndex = 0;
	}

	public boolean get(int index)
	{
		boolean b;

		if (index >= 0 && index < list.size() * 8)
			b = list.get(index / 8).getValue(index % 8);
		else
			b = false;

		return b;
	}

	//	@Override
	//	public void pack(OutputStream stream) throws IOException, PrismException
	//	{
	//		IntPacker.pack_s(stream, list.size());
	//		for (Booleans b : list)
	//			IntPacker.pack_s(stream, b.toInt());
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		clear();
	//
	//		int n = IntPacker.unpack_s(stream);
	//		for (int i = 0; i < n; ++i)
	//		{
	//			int v = IntPacker.unpack_s(stream);
	//			Booleans b = new Booleans(v);
	//			list.add(b);
	//		}
	//	}

	public int getNeededBlockCount()
	{
		int n = list.size();

		while (n > 0 && list.get(n - 1).toInt() == 0)
			--n;

		return n;
	}

	public Booleans getBlock(int index)
	{
		return list.get(index);
	}

	public void addBlock(Booleans b)
	{
		list.add(b);
	}
}
