package xoric.prism.server.control;

import xoric.prism.data.time.PrismClock;
import xoric.prism.server.model.ServerModel;

public class ServerLoop extends Thread
{
	private static ServerLoop instance;

	private final ServerModel model;
	private volatile boolean isStopRequested;

	private ServerLoop(ServerModel model)
	{
		this.model = model;
	}

	public static void startThread(ServerModel model)
	{
		requestStopThread();

		instance = new ServerLoop(model);
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

			model.net.update();

		}
		while (!isStopRequested);

		System.out.println("server loop stopped");
	}
}
