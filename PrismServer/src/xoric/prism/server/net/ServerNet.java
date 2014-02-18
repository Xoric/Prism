package xoric.prism.server.net;

import java.io.IOException;
import java.net.ServerSocket;

import xoric.prism.data.net.NetConstants;

public class ServerNet implements INetworkStatus
{
	private ServerSocket socket;

	public void startSocket() throws IOException
	{
		stopSocket();
		socket = new ServerSocket(NetConstants.port);
	}

	public void stopSocket() throws IOException
	{
		if (socket != null)
		{
			socket.close();
			socket = null;
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

	public ServerSocket getSocket()
	{
		return socket;
	}
}
