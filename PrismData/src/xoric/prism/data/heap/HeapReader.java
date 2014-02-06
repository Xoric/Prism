package xoric.prism.data.heap;

import xoric.prism.data.types.IText_r;

public class HeapReader
{
	private final Heap heap;
	private int intIndex;
	private int floatIndex;
	private int textIndex;

	public HeapReader(Heap h)
	{
		heap = h;
		intIndex = 0;
		floatIndex = 0;
		textIndex = 0;
	}

	public int getNextInt()
	{
		return heap.ints.get(intIndex++);
	}

	public float getNextFloat()
	{
		return heap.floats.get(floatIndex++);
	}

	public IText_r getNextText()
	{
		return heap.texts.get(textIndex++);
	}
}
