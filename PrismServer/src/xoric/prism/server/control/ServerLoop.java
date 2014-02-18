package xoric.prism.server.control;

import xoric.prism.data.time.PrismClock;

public class ServerLoop extends Thread
{
	private static ServerLoop instance;

	private final ILoopControl control;
	private volatile boolean isStopRequested;

	public ServerLoop(ILoopControl control)
	{
		this.control = control;
	}

	public static void startThread(ILoopControl control)
	{
		requestStopThread();

		instance = new ServerLoop(control);
		instance.start();
	}

	public static void requestStopThread()
	{
		if (instance != null)
			instance.requestStop();
	}

	public void requestStop()
	{
		isStopRequested = true;
	}

	@Override
	public void run()
	{
		PrismClock clock = new PrismClock();
		isStopRequested = false;

		System.out.println("starting server loop");

		do
		{
			//			int startMs = clock.getTimeMs();

			control.requestUpdateLoop();

		}
		while (!isStopRequested);

		System.out.println("server loop stopped");
	}
}
