package xoric.prism.data.heap;

public interface IStackable
{
	public void extractFrom(Heap h);

	public void extractFrom(HeapReader h);
}
