package xoric.prism.server.client;

import java.util.List;

import xoric.prism.com.ClientLoginMessage_in;
import xoric.prism.com.Message_in;
import xoric.prism.com.Token;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.time.PrismClock;
import xoric.prism.data.types.IText_r;
import xoric.prism.server.data.IAccount;
import xoric.prism.server.db.IAccManager;

public class ClientLink0
{
	private final ClientCore core;
	private final PrismClock clock;
	private final IAccManager accManager;

	public ClientLink0(ClientCore core, IAccManager accManager)
	{
		this.core = core;
		this.clock = new PrismClock();
		this.accManager = accManager;
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

	private boolean checkAccData(IText_r acc, byte[] pw)
	{
		boolean b;
		try
		{
			IAccount a = accManager.login(acc, pw);
			b = a != null;
		}
		catch (PrismException e)
		{
			b = false;
		}
		return b;
	}

	private void checkVersion(int index, int vExpected, int vFound)
	{
		if (vFound != vExpected)
		{
			System.err.println(toString() + " - file[" + index + "] is out of date: version " + vExpected + " expected, " + vFound
					+ " found");
		}
	}

	private void checkVersions(List<Integer> versions)
	{
		if (Prism.global.getVersionHeap().ints.size() != versions.size())
		{
			// file table is outdated
		}
		else
		{
			// file table seems up to date, check individual versions
			for (int i = 0; i < versions.size(); ++i)
			{
				checkVersion(i, Prism.global.getVersionHeap().ints.get(i), versions.get(i));
			}
		}
	}

	private void handleLogin(ClientLoginMessage_in lm) throws PrismException
	{
		try
		{
			byte[] pw = lm.getPassword();
			Heap_in h = lm.getHeap();
			IText_r acc = h.texts.get(0);
			List<Integer> versions = h.ints;

			boolean b = checkAccData(acc, pw);
			if (b)
			{
				System.out.println(toString() + " successfully logged in: " + acc.toString());

				checkVersions(versions);
			}
			else
			{
				System.err.println(toString() + " issued a failed login attempt");
			}
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.setText("error while reading login message");
			// ----
			throw e;
		}
	}

	private void handleMessage(Message_in m) throws PrismException
	{
		if (m instanceof ClientLoginMessage_in && m.getToken() == Token.LOGIN)
		{
			ClientLoginMessage_in lm = (ClientLoginMessage_in) m;
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

		Message_in m;
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
