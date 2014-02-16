package xoric.prism.data.heap;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;

public class Heap_in extends HeapBase
{
	public final List<Text> texts;

	private int intIndex;
	private int floatIndex;
	private int textIndex;

	public Heap_in()
	{
		texts = new ArrayList<Text>(5);
	}

	public Heap_in(int intCount, int floatCount, int textCount)
	{
		super(intCount, floatCount);
		texts = new ArrayList<Text>(textCount);
	}

	public Heap_in(Heap_out h)
	{
		texts = new ArrayList<Text>(h.getTextCount());

		for (int i : h.ints)
			ints.add(i);

		for (float f : h.floats)
			floats.add(f);

		for (int i = 0; i < h.getTextCount(); ++i)
			texts.add(new Text(h.getText(i)));
	}

	public void rewind()
	{
		intIndex = 0;
		floatIndex = 0;
		textIndex = 0;
	}

	public int nextInt()
	{
		return ints.get(intIndex++);
	}

	public float nextFloat()
	{
		return floats.get(floatIndex++);
	}

	public Text nextText()
	{
		return texts.get(textIndex++);
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
