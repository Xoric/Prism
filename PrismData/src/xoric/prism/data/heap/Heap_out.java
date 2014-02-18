package xoric.prism.data.heap;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IText_r;

public class Heap_out extends HeapBase
{
	public final List<IText_r> texts;

	public Heap_out()
	{
		texts = new ArrayList<IText_r>(5);
	}

	public Heap_out(int intCount, int floatCount, int textCount)
	{
		super(intCount, floatCount);
		texts = new ArrayList<IText_r>(textCount);
	}

	public Heap_out(Heap_in h)
	{
		super(h.ints.size(), h.floats.size());
		texts = new ArrayList<IText_r>(h.getTextCount());

		this.ints = h.ints;
		this.floats = h.floats;

		for (int i = 0; i < h.getTextCount(); ++i)
			texts.add(h.getText(i));

		this.bools = h.bools;
	}

	@Override
	public void clear()
	{
		super.clear();
		texts.clear();
	}

	@Override
	public IText_r getText(int index)
	{
		return texts.get(index);
	}

	@Override
	public int getTextCount()
	{
		return texts.size();
	}
}
