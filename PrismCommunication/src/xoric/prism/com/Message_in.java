package xoric.prism.com;

import java.io.IOException;
import java.io.InputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.packable.HeapPacker_s;
import xoric.prism.data.packable.IPackable_in;

public class Message_in extends MessageBase implements IPackable_in
{
	protected final Heap_in heap;

	public Message_in(Token token)
	{
		super(token);
		this.heap = new Heap_in();
	}

	@Override
	public Heap_in getHeap()
	{
		return heap;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		// first 3 bytes (startByte + size) have already been skipped at this point
		int i = stream.read();
		token = Token.valueOf(i);
		// ---
		HeapPacker_s.unpack_s(stream, token.getFloatDecimals(), heap);

		// ensure that the number of ints, floats and texts read is valid
		ensureMinima();
	}
}
