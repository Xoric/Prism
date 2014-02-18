package xoric.prism.client.net;

import java.io.IOException;
import java.net.Socket;

import xoric.prism.client.INetworkControl;
import xoric.prism.com.MessageDispatcher;
import xoric.prism.com.Message_out;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.net.NetConstants;
import xoric.prism.data.time.IUpdateListener;

/**
 * @author XoricLee
 * @since 18.02.2014, 12:54:49
 */
public class Network implements INetwork, IUpdateListener
{
	private final INetworkControl client;

	private Socket socket;
	private final MessageDispatcher dispatcher;
	private Thread connectThread;

	public Network(INetworkControl client)
	{
		this.client = client;
		this.dispatcher = new MessageDispatcher();
	}

	private synchronized void startConnectThread()
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					//			Socket socket = new Socket("127.0.0.1", NetConstants.port);
					Socket socket = new Socket("192.168.178.24", NetConstants.port);
					dispatcher.setSocket(socket);
					//				dispatcher = new MessageDispatcher(socket);
					onConnectionSuccess(socket);
				}
				catch (Exception e)
				{
					onConnectionFailed();
				}
			}
		};
		connectThread = new Thread(r);
		connectThread.start();
	}

	public boolean isConnecting()
	{
		return connectThread != null;
	}

	private synchronized void onConnectionSuccess(Socket socket)
	{
		System.out.println("connection to server established: " + socket.toString());

		try
		{
			this.socket = socket;
			this.dispatcher.setSocket(socket);
			this.connectThread = null;
		}
		catch (Exception e)
		{
			client.onNetworkException(e);
		}
	}

	private synchronized void onConnectionFailed()
	{
		System.err.println("Cannot connect to server");

		this.dispatcher.clear();
		this.connectThread = null;

		client.requestLoginScreen();
	}

	@Override
	public void send(Message_out m) throws PrismException
	{
		dispatcher.addMessage(m);

		if (socket == null && (connectThread == null || !connectThread.isAlive()))
			startConnectThread();

		//		if (connect())
		//		{
		//			System.out.println("message added to dispatcher: " + m.toString());
		//		}
		//		else
		//		{
		//			PrismException e = new PrismException();
		//			e.user.setText(UserErrorText.CONNECTION_PROBLEM);
		//			e.code.setText("error while trying to connect to server");
		//			throw e;
		//		}
	}

	@Override
	public boolean update(int passedMs)
	{
		boolean b;
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
		return b;
	}
}
