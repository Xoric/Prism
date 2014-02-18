package xoric.prism.com;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;

public class MessageDispatcher
{
	public static final int[] thresholds = { 0, 35, 500 };

	private class MessageEntry
	{
		public final Message_out message;
		public final int ageThreshold;
		public int ageMs;

		public MessageEntry(Message_out message)
		{
			this.message = message;
			this.ageThreshold = thresholds[message.getToken().getUrgency()];
		}

		public boolean isExpired()
		{
			return ageMs > ageThreshold;
		}
	}

	private final List<MessageEntry> list;
	//	private final BufferedOutputStream stream;
	private OutputStream stream;

	public MessageDispatcher()
	{
		//		this.stream = new BufferedOutputStream(socket.getOutputStream());
		//		this.stream = socket.getOutputStream();
		this.list = new ArrayList<MessageEntry>();
	}

	public void addMessage(Message_out m)
	{
		list.add(new MessageEntry(m));
	}

	public void updateAndSend(int ms) throws IOException, PrismException
	{
		if (stream != null)
		{
			boolean sendNow = false;

			for (MessageEntry e : list)
			{
				e.ageMs += ms;
				sendNow |= e.isExpired();
			}

			if (sendNow)
			{
				for (MessageEntry e : list)
				{
					e.message.pack(stream);
				}
				System.out.println(list.size() + " message(s) sent");
				list.clear();
			}
		}
	}

	public synchronized void setSocket(Socket socket) throws IOException
	{
		this.stream = socket.getOutputStream();
	}

	public synchronized void clear()
	{
		this.stream = null;
		this.list.clear();
	}
}
