package xoric.prism.data;

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

	public Text getNextText()
	{
		return heap.texts.get(textIndex++);
	}
}
