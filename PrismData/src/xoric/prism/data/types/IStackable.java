package xoric.prism.data.types;

public interface IStackable
{
	public void extractFrom(Heap h);

	public void extractFrom(HeapReader h);
}
