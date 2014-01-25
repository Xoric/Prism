package xoric.prism.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.Heap;
import xoric.prism.data.types.HeapPacker_s;
import xoric.prism.data.types.IPackable;

public class Message implements IPackable
{
	public static final byte startByte = (byte) 233;
	public static final int maximumBufferSize = 256;
	public static Perspective perspective = null;

	protected Token token;
	protected final Heap heap;

	public Message(Token token)
	{
		this.token = token;
		this.heap = new Heap();
	}

	public Heap getHeap()
	{
		return heap;
	}

	@Override
	public String toString()
	{
		return token.toString();
	}

	// ---------------------------------------------------------------
	// 	startByte		1 byte
	//	size			2 byte		** counting begins at token
	// 	token			1 byte		** size = 1 byte (token) + rest
	// ---------------------------------------------------------------

	protected int calcPackedSize()
	{
		int size = 1 /* token */
		+ HeapPacker_s.calcPackedSize_s(heap, token.getFloatDecimals()); /* heap */

		return size;
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

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		writeHeader(stream);
		stream.write(token.ordinal());
		// ---
		HeapPacker_s.pack_s(stream, heap, token.getFloatDecimals());
	}

	protected void writeHeader(OutputStream stream) throws IOException
	{
		int size = calcPackedSize();
		stream.write(startByte);
		stream.write(size);
		stream.write(size >> 8);
	}

	public Token getToken()
	{
		return token;
	}

	@SuppressWarnings("rawtypes")
	private void checkList(String name, List list, int min) throws PrismException
	{
		final int n = list == null ? 0 : list.size();

		if (n < min)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.COMMUNICATION_ERROR);
			// ----
			e.code.setText("message " + token.toString() + " has too few " + name, min, n);
			// ----
			throw e;
		}
	}

	protected void ensureMinima() throws PrismException
	{
		Minima m;
		if (perspective == Perspective.SERVER)
			m = token.getClientMinima();
		else if (perspective == Perspective.CLIENT)
			m = token.getServerMinima();
		else
			m = null;

		if (m != null)
		{
			checkList("ints", heap.ints, m.intMin);
			checkList("floats", heap.floats, m.floatMin);
			checkList("texts", heap.texts, m.textMin);
		}
	}
}