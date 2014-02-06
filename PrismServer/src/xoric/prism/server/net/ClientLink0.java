package xoric.prism.server.net;

import java.util.List;

import xoric.prism.com.ClientLoginMessage;
import xoric.prism.com.Message;
import xoric.prism.com.Token;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap;
import xoric.prism.data.time.PrismClock;
import xoric.prism.data.types.IText_r;

public class ClientLink0
{
	private final ClientCore core;
	private final PrismClock clock;

	public ClientLink0(ClientCore core)
	{
		this.core = core;
		this.clock = new PrismClock();
	}

	public void kick()
	{
		core.kick();
	}

	public PrismClock getClock()
	{
		return clock;
	}

	public ClientCore getCore()
	{
		return core;
	}

	@Override
	public String toString()
	{
		return core.toString() + "/0";
	}

	private void handleLogin(ClientLoginMessage lm)
	{
		byte[] pw = lm.getPassword();
		Heap h = lm.getHeap();
		IText_r acc = h.texts.get(0);
		List<Integer> versions = h.ints;

		System.out.println("I (" + toString() + ") want to login with acc=" + acc.toString());
	}

	private void handleMessage(Message m) throws PrismException
	{
		if (m instanceof ClientLoginMessage && m.getToken() == Token.LOGIN)
		{
			ClientLoginMessage lm = (ClientLoginMessage) m;
			handleLogin(lm);
		}
		else
		{
			PrismException e = new PrismException();
			// ----
			String s = m == null ? "null" : m.getToken().toString();
			e.setText("unexpected message received (" + s + ")");
			// ----
			throw e;
		}
	}

	public void handleMessages() throws Exception
	{
		Exception e = core.getCrashException();
		if (e != null)
			throw e;

		Message m;
		do
		{
			m = core.getNextMessage();
			if (m != null)
				handleMessage(m);
		}
		while (m != null);
	}

	public boolean isLoggedIn()
	{
		return false;
	}
}
