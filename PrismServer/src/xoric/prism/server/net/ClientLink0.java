package xoric.prism.server.net;

import java.io.IOException;

import xoric.prism.data.time.PrismClock;

public class ClientLink0
{
	private final ClientCore core;
	private final PrismClock clock;

	public ClientLink0(ClientCore core)
	{
		this.core = core;
		this.clock = new PrismClock();
	}

	public void kick() throws IOException
	{
		core.kick();
	}

	public PrismClock getClock()
	{
		return clock;
	}

	@Override
	public String toString()
	{
		return core.toString() + " (" + clock.getTimeMs() + " ms)";
	}
}
