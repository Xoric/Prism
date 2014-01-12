package xoric.prism.swing.multithread;

public abstract class PrismWorker implements Runnable
{
	private final String title;
	protected volatile boolean isDone;
	protected volatile float progress;
	protected volatile String text;

	public PrismWorker(String title)
	{
		this.title = title;
		this.isDone = false;
		this.text = "Waiting...";
		this.progress = 0.0f;
	}

	public float getProgress()
	{
		return progress;
	}

	public String getText()
	{
		return text;
	}

	public String getTitle()
	{
		return title;
	}

	public boolean isDone()
	{
		return isDone;
	}
}
