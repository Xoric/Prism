package xoric.prism.data.meta;

import xoric.prism.data.heap.HeapBase;

abstract class MetaLineBase
{
	protected MetaKey key;

	protected abstract HeapBase getHeap();

	@Override
	public String toString()
	{
		return "key=" + key.toString() + ", heap=" + getHeap().toString();
	}

	public MetaKey getKey()
	{
		return key;
	}
}
