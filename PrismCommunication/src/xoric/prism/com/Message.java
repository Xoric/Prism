package xoric.prism.com;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.HeapBase;

abstract class MessageBase
{
	public static final byte startByte = (byte) 233;
	public static final int maximumBufferSize = 256;
	public static Perspective perspective = null;

	protected Token token;

	public MessageBase(Token token)
	{
		this.token = token;
	}

	public abstract HeapBase getHeap();

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

	public Token getToken()
	{
		return token;
	}

	@SuppressWarnings("rawtypes")
	private void checkList(String name, int n, int min) throws PrismException
	{
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
			checkList("ints", getHeap().ints.size(), m.intMin);
			checkList("floats", getHeap().floats.size(), m.floatMin);
			checkList("texts", getHeap().getTextCount(), m.textMin);
		}
	}
}
