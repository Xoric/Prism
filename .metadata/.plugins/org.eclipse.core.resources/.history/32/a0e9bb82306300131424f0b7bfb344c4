package xoric.prism.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.Heap;
import xoric.prism.data.HeapPacker;
import xoric.prism.data.IPackable;

public class MetaLine implements IPackable
{
	private final byte[] token;
	private final Heap heap;
	private final HeapPacker heapPacker;

	public MetaLine()
	{
		this.token = new byte[1];
		this.token[0] = 0;
		this.heap = new Heap();
		this.heapPacker = new HeapPacker();
		this.heapPacker.setHeap(heap);
	}

	public MetaLine(char token)
	{
		this.token = new byte[1];
		this.token[0] = (byte) token;
		this.heap = new Heap();
		this.heapPacker = new HeapPacker();
		this.heapPacker.setHeap(heap);
	}

	public char getToken()
	{
		return (char) token[0];
	}

	public Heap getHeap()
	{
		return heap;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		stream.write(token);
		heapPacker.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		stream.read(token);
		heapPacker.unpack(stream);
	}

	@Override
	public int getPackedSize()
	{
		int size = 1 /*token*/+ heapPacker.getPackedSize();
		return size;
	}
}
