package xoric.prism.swing;

import java.util.Timer;
import java.util.TimerTask;

public class PrismTimer extends TimerTask
{
	private static final int interval = 25;

	private final ITimerListener listener;
	private final Timer timer;
	private long lastMs;

	public PrismTimer(ITimerListener listener)
	{
		this.listener = listener;
		this.timer = new Timer();
	}

	public void start()
	{
		lastMs = System.currentTimeMillis();
		timer.schedule(this, 0, interval);
	}

	public void stop()
	{
		timer.cancel();
	}

	@Override
	public void run()
	{
		long currentMs = System.currentTimeMillis();
		int passedMs = (int) (currentMs - lastMs);

		listener.onTimerTick(passedMs);

		lastMs = currentMs;
	}
}
