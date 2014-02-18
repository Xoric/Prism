package xoric.prism.server.control;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.server.client.ClientCore;
import xoric.prism.server.client.ClientLink0;
import xoric.prism.server.db.IAccManager;

public class Doorman implements IDoorman, ILoopListener
{
	private volatile List<ClientLink0> links;
	private IAccManager accManager;

	public Doorman()
	{
		links = new ArrayList<ClientLink0>();
	}

	public void register(IAccManager accManager)
	{
		this.accManager = accManager;
	}

	@Override
	public synchronized void update()
	{
		for (int i = links.size() - 1; i >= 0; --i)
			handleClient(i);
	}

	private void handleClient(int index)
	{
		ClientLink0 client0 = links.get(index);
		String kickReason = null;
		try
		{
			client0.handleMessages();
			boolean isLoggedIn = client0.isLoggedIn();

			if (isLoggedIn)
			{
			}
			else
			{
				int ms = client0.getClock().getTimeMs();
				if (ms >= 3000)
					kickReason = "doorman kicked " + client0 + " for inactivity (" + ms + " ms)";
			}
		}
		catch (Exception e)
		{
			kickReason = "doorman watched " + client0 + " crash" + getMessage(e);
		}

		if (kickReason != null)
		{
			client0.kick();
			links.remove(client0);
			System.err.println(kickReason);
		}
	}

	private static String getMessage(Exception e)
	{
		String s;
		if (e instanceof PrismException)
			s = ": " + ((PrismException) e).code.getText();
		else if (e != null)
			s = ": " + e.toString();
		else
			s = "";

		return s;
	}

	@Override
	public synchronized void passClient(ClientCore client)
	{
		ClientLink0 c = new ClientLink0(client, accManager);
		links.add(c);
	}
}
