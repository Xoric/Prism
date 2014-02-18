package xoric.prism.server.control;

import java.io.IOException;

import xoric.prism.server.net.Receptionist;
import xoric.prism.server.net.ServerNet;
import xoric.prism.server.view.IServerView;

public class ServerControl implements IServerControl
{
	private IServerView view;
	private Doorman doorman;
	private Receptionist receptionist;
	private ServerNet network;

	public void register(IServerView view, Doorman doorman, ServerNet network)
	{
		this.view = view;
		this.doorman = doorman;
		this.network = network;
	}

	/*
	 * ILoopControl
	 */
	@Override
	public void requestUpdateLoop()
	{
		doorman.update();
	}

	/*
	 * INetControl
	 */
	@Override
	public void requestStartServer()
	{
		// start socket and receptionist
		if (!network.isActive())
		{
			try
			{
				network.startSocket();
				receptionist = new Receptionist(network.getSocket(), doorman);
				receptionist.start();

				System.out.println("server started");
			}
			catch (IOException e0)
			{
				System.err.print("error while activating server socket (" + e0.getMessage() + ")");
			}
		}

		// update view
		view.getNetView().displayNetState();

		// start server loop
		ServerLoop.startThread(this);
	}

	@Override
	public void requestStopServer()
	{
		// stop server loop
		ServerLoop.requestStopThread();

		// stop receptionist and socket
		try
		{
			receptionist.interrupt();
			network.stopSocket();
			System.out.println("server stopped");
		}
		catch (IOException e0)
		{
			System.err.print("error while closing server socket (" + e0.getMessage() + ")");
		}

		// update view
		view.getNetView().displayNetState();
	}
}
