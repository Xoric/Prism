package xoric.prism.ui;

import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.PrismColor;

/**
 * @author Felix Möhrle
 * @since 28.05.2011, 15:56:42
 */
public class BlinkColor implements IUpdateListener
{
	public static BlinkColor cursorColor;
	public static BlinkColor selectionColor = new BlinkColor(new PrismColor(1.0f, 0.6f, 0.0f, 1.0f),
			new PrismColor(1.0f, 0.6f, 0.0f, 0.5f), 500); // TODO temp

	private int durationMs;
	private int ms;
	private boolean isAdding;

	private PrismColor color0;
	private PrismColor color1;
	private PrismColor mixedColor;

	/**
	 * BlinkColor constructor. Copies the references of color0 and color1.
	 * @param color0
	 * @param color1
	 */
	public BlinkColor(PrismColor color0, PrismColor color1, int durationMs)
	{
		this.isAdding = true;
		this.color0 = color0;
		this.color1 = color1;
		this.mixedColor = new PrismColor();
		this.durationMs = durationMs;
		update(0);
	}

	public PrismColor getMixedColor()
	{
		return mixedColor;
	}

	@Override
	public boolean update(int passedMs)
	{
		if (isAdding)
		{
			ms += passedMs;

			if (ms >= durationMs)
			{
				ms = durationMs;
				isAdding = false;
			}
		}
		else
		{
			ms -= passedMs;

			if (ms <= 0)
			{
				ms = 0;
				isAdding = true;
			}
		}

		float fac1 = (float) ms / (float) durationMs;
		float fac0 = 1.0f - fac1;

		for (int i = 0; i < 4; ++i)
			mixedColor.setComponent(i, color0.getComponent_f(i) * fac0 + color1.getComponent_f(i) * fac1);

		return true;
	}
}
