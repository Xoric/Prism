package xoric.prism.com;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Non-blocking thread-safe list. Base on W. Tichy and D. Meder, KIT Karlsruhe.
 * @author XoricLee
 */
public class MessagePipe
{
	private class MessageNode
	{
		private Message message;
		private volatile MessageNode next;

		public MessageNode(Message m)
		{
			this.message = m;
			this.next = null;
		}
	}

	// head of the queue: consume data here
	private MessageNode head;
	private AtomicBoolean consumerLock;

	// tail of the queue: produce data here
	private MessageNode tail;
	private AtomicBoolean producerLock;

	public MessagePipe()
	{
		head = tail = new MessageNode(null);
		producerLock = new AtomicBoolean(false);
		consumerLock = new AtomicBoolean(false);
	}

	public void produce(final Message m)
	{
		// create new node
		MessageNode tmp = new MessageNode(m);

		// wait until queue is available
		while (!producerLock.compareAndSet(false, true))
		{
		}

		// create and append new node 
		tail.next = tmp;
		tail = tmp;

		// release lock
		producerLock.getAndSet(false);
	}

	public Message consume()
	{
		// wait until queue is available
		while (!consumerLock.compareAndSet(false, true))
		{
		}

		// get next node	
		Message result;
		MessageNode nextNode = head.next;

		if (nextNode != null)
		{
			// take first element if any
			result = nextNode.message;
			nextNode.message = null;

			// make next node the new head
			head = nextNode;

			// release lock
			consumerLock.getAndSet(false);
		}
		else
		{
			// queue is empty
			result = null;
		}

		// release lock and return element
		consumerLock.getAndSet(false);
		return result;
	}
}
