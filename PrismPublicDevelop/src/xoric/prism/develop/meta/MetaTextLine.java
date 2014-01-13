package xoric.prism.develop.meta;

import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.types.Heap;
import xoric.prism.data.types.Text;

class MetaTextLine
{
	private final String key;
	private final String[] params;

	public MetaTextLine(String key, String[] params)
	{
		this.key = key.toUpperCase();
		this.params = params;
	}

	public MetaLine toMetaLine()
	{
		MetaKey k = MetaKey.valueOf(key);

		MetaLine l = new MetaLine(k);
		Heap heap = l.getHeap();

		for (String s : params)
			addParam(s, heap);

		return l;
	}

	private void addParam(String s, Heap heap)
	{
		// try to convert to int
		try
		{
			int i = Integer.valueOf(s);
			heap.ints.add(i);
			return;
		}
		catch (Exception e0)
		{
		}

		// try to convert to float
		try
		{
			float f = Float.valueOf(s);
			heap.floats.add(f);
			return;
		}
		catch (Exception e0)
		{
		}

		// assume text
		Text t = new Text(s);
		heap.texts.add(t);
	}
}
