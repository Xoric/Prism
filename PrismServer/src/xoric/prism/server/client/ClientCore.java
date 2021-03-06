package xoric.prism.server.client;

import java.io.IOException;
import java.net.Socket;

import xoric.prism.com.ISocketListener;
import xoric.prism.com.MessagePipe;
import xoric.prism.com.Message_in;
import xoric.prism.com.SocketReader;

public class ClientCore implements ISocketListener
{
	private final Socket socket;
	private final String name;
	private final SocketReader reader;
	private final MessagePipe messagePipe;
	private volatile Exception crashException;

	private ClientCore(Socket socket) throws IOException
	{
		this.socket = socket;
		this.reader = new SocketReader(socket, this);
		this.messagePipe = new MessagePipe();

		String s = socket.getInetAddress().toString();
		if (s.length() > 0 && s.charAt(0) == '/')
			s = s.substring(1);
		name = s;
	}

	public void kick()
	{
		try
		{
			reader.interrupt();
		}
		catch (Exception e)
		{
		}

		try
		{
			socket.close();
		}
		catch (Exception e)
		{
		}
	}

	public Socket getSocket()
	{
		return socket;
	}

	@Override
	public String toString()
	{
		return socket == null ? "null-socket" : name;
	}

	/**
	 * Returns an Exception if this client has crashed. Returns null otherwise.
	 * @return Exception
	 */
	public Exception getCrashException()
	{
		return crashException;
	}

	@Override
	public void onSocketListenerCrashing(Exception e)
	{
		if (e == null)
			e = new Exception("unknown error");

		this.crashException = e;
	}

	@Override
	public void receiveMessage(Message_in m)
	{
		messagePipe.produce(m);
	}

	public Message_in getNextMessage()
	{
		return messagePipe.consume();
	}

	public static ClientCore createAndListen(Socket socket) throws IOException
	{
		ClientCore client = new ClientCore(socket);
		client.reader.start();
		return client;
	}
}
