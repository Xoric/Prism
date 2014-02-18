package xoric.prism.scene.lwjgl;

import xoric.prism.scene.IInputListener;

/**
 * @author XoricLee
 * @since 17.02.2014, 23:16:25
 */
public class KeyRepeater
{
	private static final int initialDelay = 500;
	private static final int tickDelay = 75;

	private IInputListener listener;

	private int delay;

	private int key;
	private char keyChar;
	private int keyCode;
	private int mode;

	public void update(int passedMs)
	{
		delay -= passedMs;

		if (delay <= 0)
		{
			delay = tickDelay;

			if (mode == 1)
				listener.onControlKey(keyCode, true);
			else if (mode == 2)
				listener.onCharacterKey(keyChar, true);
		}
	}

	public boolean isActive()
	{
		return key >= 0;
	}

	public int getKey()
	{
		return key;
	}

	public void setKeyCode(int key, int keyCode)
	{
		this.key = key;
		this.keyCode = keyCode;
		mode = 1;
		delay = initialDelay;
	}

	public void setKeyChar(int key, char keyChar)
	{
		this.key = key;
		this.keyChar = keyChar;
		mode = 2;
		delay = initialDelay;
	}

	public void clear()
	{
		key = -1;
		mode = 0;
	}

	public void setListener(IInputListener listener)
	{
		this.listener = listener;
	}
}
