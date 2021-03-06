package xoric.prism.data.meta;

import java.io.IOException;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.packable.HeapPacker_s;
import xoric.prism.data.packable.IPackable_out;
import xoric.prism.data.packable.IntPacker;

public class MetaLine_out extends MetaLineBase implements IPackable_out
{
	private final Heap_out heap;

	public MetaLine_out(MetaKey key)
	{
		this.key = key;
		this.heap = new Heap_out();
	}

	public MetaLine_out(MetaLine_in l)
	{
		this.key = l.key;
		this.heap = new Heap_out(l.getHeap());
	}

	@Override
	public Heap_out getHeap()
	{
		return heap;
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		IntPacker.pack_s(stream, key.ordinal());
		HeapPacker_s.pack_s(stream, heap, 2);
	}
}
