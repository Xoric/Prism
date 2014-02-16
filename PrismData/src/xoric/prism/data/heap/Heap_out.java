package xoric.prism.data.heap;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;

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

		for (int i : h.ints)
			ints.add(i);

		for (float f : h.floats)
			floats.add(f);

		for (int i = 0; i < h.getTextCount(); ++i)
			texts.add(new Text(h.getText(i)));
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
