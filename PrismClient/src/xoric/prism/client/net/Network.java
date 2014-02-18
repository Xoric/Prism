package xoric.prism.client.net;

import java.io.IOException;
import java.net.Socket;

import xoric.prism.com.MessageDispatcher;
import xoric.prism.com.Message_out;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.net.NetConstants;
import xoric.prism.data.time.IUpdateListener;

/**
 * @author XoricLee
 * @since 18.02.2014, 12:54:49
 */
public class Network implements INetwork, IUpdateListener
{
	private Socket socket;
	private MessageDispatcher dispatcher;

	@Override
	public boolean connect()
	{
		if (socket == null)
		{
			try
			{
				//			Socket socket = new Socket("127.0.0.1", NetConstants.port);
				socket = new Socket("192.168.178.24", NetConstants.port);
				dispatcher = new MessageDispatcher(socket);
				System.out.println("connection to server established: " + socket.toString());
			}
			catch (Exception e)
			{
				System.err.println("Cannot connect to server");
				socket = null;
				dispatcher = null;
			}
		}
		return socket != null;
	}

	@Override
	public void send(Message_out m) throws PrismException
	{
		if (connect())
		{
			dispatcher.addMessage(m);
			System.out.println("message added to dispatcher: " + m.toString());
		}
		else
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.CONNECTION_PROBLEM);
			e.code.setText("error while trying to connect to server");
			throw e;
		}
	}

	@Override
	public boolean update(int passedMs)
	{
		boolean b;
		if (dispatcher != null)
		{
			try
			{
				dispatcher.updateAndSend(passedMs);
				b = true;
			}
			catch (IOException e)
			{
				b = false;
				e.printStackTrace();
			}
			catch (PrismException e)
			{
				b = false;
				e.code.print();
				e.user.showMessage();
			}
		}
		else
			b = true;

		return b;
	}
}
