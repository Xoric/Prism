package xoric.prism.com;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public class SocketReader extends Thread
{
	private final BufferedInputStream stream;
	private final byte[] buffer;
	private final ISocketListener listener;

	public SocketReader(Socket socket, ISocketListener listener) throws IOException
	{
		this.stream = new BufferedInputStream(socket.getInputStream());
		this.buffer = new byte[Message.maximumBufferSize];
		this.listener = listener;
	}

	@Override
	public void run()
	{
		try
		{
			listen();
		}
		catch (Exception e)
		{
			listener.onSocketListenerCrashing(e);
		}
	}

	private void readBytes(int count) throws IOException
	{
		int n = 0;
		do
		{
			n += stream.read(buffer, n, count - n);
		}
		while (n < count);
	}

	private void listen() throws Exception
	{
		do
		{
			// read header
			readBytes(3);

			// check startByte
			if (buffer[0] != Message.startByte)
			{
				PrismException e = new PrismException();
				// ----
				e.user.setText(UserErrorText.COMMUNICATION_ERROR);
				// ----
				e.code.setText("forbidden buffer start");
				// ----
				throw e;
			}

			// extract size
			int size = buffer[1] | (buffer[2] << 8);
			if (size < 1 || size > Message.maximumBufferSize)
			{
				PrismException e = new PrismException();
				// ----
				e.user.setText(UserErrorText.COMMUNICATION_ERROR);
				// ----
				e.code.setText("buffer supplied invalid size (" + size + " bytes)");
				// ----
				throw e;
			}

			// read actual data
			readBytes(size);

			// peek tokenIndex
			int tokenIndex = buffer[0];
			ByteArrayInputStream stream = new ByteArrayInputStream(buffer, 0, size);

			// create message
			Message m;
			if (tokenIndex == Token.LOGIN.ordinal())
			{
				m = new ClientLoginMessage();
			}
			else
			{
				Token token = Token.valueOf(tokenIndex);
				m = new Message(token);
			}
			m.unpack(stream);
			listener.receiveMessage(m);
		}
		while (true);
	}
}
