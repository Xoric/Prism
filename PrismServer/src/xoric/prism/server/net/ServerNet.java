package xoric.prism.server.net;

import java.io.IOException;
import java.net.ServerSocket;

import xoric.prism.data.net.NetConstants;

public class ServerNet implements IServerNet
{
	private ServerSocket socket;
	private Receptionist receptionist;

	@Override
	public void start() throws IOException
	{
		stop();

		socket = new ServerSocket(NetConstants.port);
		receptionist = new Receptionist(socket);
		receptionist.start();
	}

	@Override
	public void stop() throws IOException
	{
		if (socket != null)
		{
			receptionist.interrupt();
			socket.close();
		}
	}

	@Override
	public String getPort()
	{
		return socket == null ? "null" : String.valueOf(socket.getLocalPort());
	}

	@Override
	public boolean isActive()
	{
		return socket != null && socket.isBound() && !socket.isClosed();
	}
}
