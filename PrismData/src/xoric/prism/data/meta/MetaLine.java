package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.types.Heap;
import xoric.prism.data.types.HeapPacker_s;
import xoric.prism.data.types.IPackable;

public class MetaLine implements IPackable
{
	private final byte[] keyBuf;
	private final MetaKey key;
	private Heap heap;

	public MetaLine()
	{
		this.key = MetaKey.ITEM;
		this.keyBuf = new byte[1];
		this.keyBuf[0] = key.toByte();
		this.heap = new Heap();
	}

	public MetaLine(MetaKey key)
	{
		this.key = key;
		this.keyBuf = new byte[1];
		this.keyBuf[0] = key.toByte();
		this.heap = new Heap();
	}

	@Override
	public String toString()
	{
		return key.toString() + " " + heap.toString();
	}

	public MetaKey getKey()
	{
		return key;
	}

	public Heap getHeap()
	{
		return heap;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		stream.write(keyBuf);
		HeapPacker_s.pack_s(stream, heap, 2);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		stream.read(keyBuf);
		heap = HeapPacker_s.unpack_s(stream, 2);
	}

	//	@Override
	//	public int getPackedSize()
	//	{
	//		int size = 1 /*token*/+ HeapPacker_s.getPackedSize_s(heap, 2);
	//		return size;
	//	}
}
