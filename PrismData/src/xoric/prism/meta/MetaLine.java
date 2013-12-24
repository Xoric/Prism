package xoric.prism.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.Heap;
import xoric.prism.data.HeapPacker;
import xoric.prism.data.IPackable;

public class MetaLine implements IPackable
{
	private final byte[] keyBuf;
	private final MetaKey key;
	private final Heap heap;
	private final HeapPacker heapPacker;

	public MetaLine()
	{
		this.key = MetaKey.COMMON;
		this.keyBuf = new byte[1];
		this.keyBuf[0] = key.toByte();
		this.heap = new Heap();
		this.heapPacker = new HeapPacker();
		this.heapPacker.setHeap(heap);
	}

	public MetaLine(MetaKey key)
	{
		this.key = key;
		this.keyBuf = new byte[1];
		this.keyBuf[0] = key.toByte();
		this.heap = new Heap();
		this.heapPacker = new HeapPacker();
		this.heapPacker.setHeap(heap);
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
		heapPacker.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		stream.read(keyBuf);
		heapPacker.unpack(stream);
	}

	@Override
	public int getPackedSize()
	{
		int size = 1 /*token*/+ heapPacker.getPackedSize();
		return size;
	}
}
