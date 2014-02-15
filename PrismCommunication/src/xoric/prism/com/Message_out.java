package xoric.prism.com;

import java.io.IOException;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.packable.HeapPacker_s;
import xoric.prism.data.packable.IPackable_out;

public class Message_out extends MessageBase implements IPackable_out
{
	protected final Heap_out heap;

	public Message_out(Token token)
	{
		super(token);
		this.heap = new Heap_out();
	}

	@Override
	public Heap_out getHeap()
	{
		return heap;
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		writeHeader(stream);
		stream.write(token.ordinal());
		// ---
		HeapPacker_s.pack_s(stream, heap, token.getFloatDecimals());
	}

	protected void writeHeader(OutputStream stream) throws IOException, PrismException
	{
		int size = calcPackedSize();
		stream.write(startByte);
		stream.write(size);
		stream.write(size >> 8);
	}

	protected int calcPackedSize() throws PrismException
	{
		int size = 1 /* token */
		+ HeapPacker_s.calcPackedSize_s(getHeap(), token.getFloatDecimals()); /* heap */

		return size;
	}
}
