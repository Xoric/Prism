package xoric.prism.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import xoric.prism.server.client.ClientCore;
import xoric.prism.server.control.IDoorman;

public class Receptionist extends Thread
{
	private final ServerSocket socket;
	private final IDoorman doorman;

	public Receptionist(ServerSocket socket, IDoorman doorman)
	{
		this.socket = socket;
		this.doorman = doorman;
	}

	@Override
	public void run()
	{
		System.out.println("receptionist listening");
		try
		{
			do
			{
				Socket clientSocket = socket.accept();
				ClientCore client = ClientCore.createAndListen(clientSocket);

				System.out.println("receptionist welcomes " + client.toString());

				doorman.passClient(client);
			}
			while (true);
		}
		catch (IOException e)
		{
			System.err.println("receptionist stopped (" + e.getMessage() + ")");
		}
	}
}
