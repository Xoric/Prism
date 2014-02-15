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

	@Override
	public void clear()
	{
		super.clear();
		texts.clear();
	}

	@Override
	protected IText_r getText(int index)
	{
		return texts.get(index);
	}

	@Override
	protected int getTextCount()
	{
		return texts.size();
	}
}
