package xoric.prism.server.net;

import java.io.IOException;
import java.net.Socket;

public class ClientCore
{
	private final Socket socket;

	public ClientCore(Socket socket)
	{
		this.socket = socket;
	}

	public void kick() throws IOException
	{
		if (socket.isBound() && !socket.isClosed())
			socket.close();
	}

	public Socket getSocket()
	{
		return socket;
	}

	@Override
	public String toString()
	{
		return socket == null ? "null-socket" : socket.toString();
	}
}
