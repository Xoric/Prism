package xoric.prism.com;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessageDispatcher
{
	public static final int[] thresholds = { 0, 35, 500 };

	private class MessageEntry
	{
		public final Message message;
		public final int threshold;
		public int ageMs;

		public MessageEntry(Message message)
		{
			this.message = message;
			this.threshold = thresholds[message.getToken().getUrgency()];
		}

		public boolean isExpired()
		{
			return ageMs > threshold;
		}
	}

	private final List<MessageEntry> list;
	private final BufferedOutputStream stream;

	public MessageDispatcher(Socket socket) throws IOException
	{
		this.stream = new BufferedOutputStream(socket.getOutputStream());
		this.list = new ArrayList<MessageEntry>();
	}

	public void addMessage(Message m)
	{
		list.add(new MessageEntry(m));
	}

	public void updateAndSend(int ms) throws IOException
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
				e.message.pack(stream);

			list.clear();
		}
	}
}