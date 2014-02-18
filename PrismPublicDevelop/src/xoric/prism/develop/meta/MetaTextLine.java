package xoric.prism.develop.meta;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaLine_out;
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

	public MetaLine_in toMetaLine_in(IInfoLayer uplink)
	{
		MetaLine_out l = toMetaLine_out();
		return new MetaLine_in(uplink, l);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.length; ++i)
		{
			if (i > 0)
				sb.append(", ");

			sb.append(params[i]);
		}
		return "key=" + key + ", params: " + sb.toString();
	}

	private void addParam(String s, Heap_out heap)
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

	public MetaLine_out toMetaLine_out()
	{
		MetaKey k = MetaKey.valueOf(key);

		MetaLine_out l = new MetaLine_out(k);
		Heap_out heap = l.getHeap();

		for (String s : params)
			addParam(s, heap);

		return l;
	}
}
