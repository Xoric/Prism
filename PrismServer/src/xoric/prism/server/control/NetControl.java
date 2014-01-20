package xoric.prism.server.control;

import java.io.IOException;

import xoric.prism.server.net.IServerNet;

public abstract class NetControl
{
	public static void startNet(IServerNet net)
	{
		try
		{
			net.start();
			System.out.println("server started");
		}
		catch (IOException e0)
		{
			System.err.print("error while activating server socket (" + e0.getMessage() + ")");
		}
	}

	public static void stopNet(IServerNet net)
	{
		try
		{
			net.stop();
			System.out.println("server stopped");
		}
		catch (IOException e0)
		{
			System.err.print("error while closing server socket (" + e0.getMessage() + ")");
		}
	}
}
