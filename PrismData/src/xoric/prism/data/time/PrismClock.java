package xoric.prism.data.time;

public class PrismClock
{
	private long zero;

	public PrismClock()
	{
		calibrate(0);
	}

	public void calibrate(int ms)
	{
		zero = System.currentTimeMillis() - ms;
	}

	public int getTimeMs()
	{
		int ms = (int) (System.currentTimeMillis() - zero);

		return ms;
	}
}
