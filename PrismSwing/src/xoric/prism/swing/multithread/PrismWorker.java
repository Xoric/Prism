package xoric.prism.swing.multithread;

public abstract class PrismWorker implements Runnable
{
	protected volatile float progress;
	protected volatile String text;

	public PrismWorker(String initialText)
	{
		text = initialText;
		progress = 0.0f;
	}

	public float getProgress()
	{
		return progress;
	}

	public String getText()
	{
		return text;
	}
}
