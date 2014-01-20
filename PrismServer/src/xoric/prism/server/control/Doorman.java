package xoric.prism.server.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.server.net.ClientLink0;

public class Doorman
{
	private final List<ClientLink0> links;

	public Doorman()
	{
		links = new ArrayList<ClientLink0>();
	}

	public void addClient(ClientLink0 client)
	{
		links.add(client);
	}

	private void kickClient(ClientLink0 client)
	{
		try
		{
			client.kick();
			System.out.println("doorman kicked " + client);
		}
		catch (IOException e)
		{
			System.err.println("doorman encountered an error trying to kick " + client);
		}
	}

	public void update()
	{
		for (int i = links.size() - 1; i >= 0; --i)
		{
			ClientLink0 c = links.get(i);
			if (c.getClock().getTimeMs() > 3000)
			{
				kickClient(c);
				links.remove(i);
			}
		}
	}
}