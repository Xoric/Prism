package xoric.prism.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receptionist extends Thread
{
	private final ServerSocket socket;

	public Receptionist(ServerSocket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		try
		{
			do
			{
				Socket client = socket.accept();
				System.out.print("receptionist received connection: " + client.getInetAddress());
			}
			while (true);
		}
		catch (IOException e)
		{
			System.err.println("receptionist stopped");
		}
	}
}
