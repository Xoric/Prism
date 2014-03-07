package xoric.prism.scene.lwjgl;

import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.scene.IInputListener;

public class InputHandlerLWJGL
{
	private int buttonCount;
	private boolean[] isButtonDown;

	private IInputListener listener;
	private IFloatPoint_r frameSize;
	private int windowHeight;

	private final Point mouseInt; // in pixels (int)
	private final FloatPoint mouseOnScreen; // in pixels (float)

	private final KeyRepeater repeater;

	public InputHandlerLWJGL()
	{
		mouseInt = new Point();
		mouseOnScreen = new FloatPoint();
		repeater = new KeyRepeater();

	}

	public void initialize(IInputListener listener, IFloatPoint_r frameSize)//int windowHeight)
	{
		buttonCount = Mouse.getButtonCount();
		if (buttonCount <= 0)
			buttonCount = 5;
		isButtonDown = new boolean[buttonCount];

		this.listener = listener;
		this.repeater.setListener(listener);
		this.windowHeight = (int) frameSize.getY();// windowHeight;
		this.frameSize = frameSize;
	}

	public IFloatPoint_r getMouseOnScreen()
	{
		return mouseOnScreen;
	}

	public void update(int passedMs)
	{
		// get mouse coordinates
		int x = Mouse.getX();
		int y = windowHeight - Mouse.getY();

		if (x != mouseInt.x || y != mouseInt.y)
		{
			mouseInt.x = x;
			mouseInt.y = y;

			mouseOnScreen.x = x;
			mouseOnScreen.y = y;

			listener.onMouseMove();
		}

		// check mouse buttons
		for (int b = 0; b < buttonCount; ++b)
		{
			if (!isButtonDown[b] && Mouse.isButtonDown(b))
			{
				listener.onMouseDown(b);
				isButtonDown[b] = true;
			}
			else if (isButtonDown[b] && !Mouse.isButtonDown(b))
			{
				listener.onMouseUp(b);
				isButtonDown[b] = false;
			}
		}

		// check if an arrow button is being hold down
		if (repeater.isActive())
		{
			if (Keyboard.isKeyDown(repeater.getKey()))
				repeater.update(passedMs);
			else
				repeater.clear();
		}

		//		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_X); // is x key down.
		while (Keyboard.next())
		{
			//			if (Keyboard.getEventKeyState())
			//			{
			boolean isDown = Keyboard.getEventKeyState();

			int key = Keyboard.getEventKey();
			//			String s = Keyboard.getKeyName(key);
			char c = Keyboard.getEventCharacter();
			//				int keyCode = c;

			handleKey(key, c, isDown);

			//			System.out.println("key=" + key + ", s=" + s + ", c=" + c + "(" + (int) c + ")");
			//			}
			//			if (Keyboard.getEventKeyState())
			//			{
			//				if (Keyboard.getEventKey() == Keyboard.KEY_A)
			//				{
			//					System.out.println("A Key Pressed");
			//				}
			//			}
			//			else
			//			{
			//				if (Keyboard.getEventKey() == Keyboard.KEY_A)
			//				{
			//					System.out.println("A Key Released");
			//				}
			//			}
		}
	}

	private void handleKey(int key, char c, boolean isDown)
	{
		int i = -1;
		repeater.clear();

		if (key == 42 || key == 54)
			i = KeyEvent.VK_SHIFT;
		else if (key == 29 || key == 157)
			i = KeyEvent.VK_CONTROL;
		else if (key == 56 || key == 184)
			i = KeyEvent.VK_ALT;
		else if (key == 199)
			i = KeyEvent.VK_HOME;
		else if (key == 207)
			i = KeyEvent.VK_END;
		else if (key == 211)
		{
			i = KeyEvent.VK_DELETE;
			repeater.setKeyCode(key, i);
		}
		else if (key == 200)
		{
			i = KeyEvent.VK_UP;
			repeater.setKeyCode(key, i);
		}
		else if (key == 203)
		{
			i = KeyEvent.VK_LEFT;
			repeater.setKeyCode(key, i);
		}
		else if (key == 205)
		{
			i = KeyEvent.VK_RIGHT;
			repeater.setKeyCode(key, i);
		}
		else if (key == 208)
		{
			i = KeyEvent.VK_DOWN;
			repeater.setKeyCode(key, i);
		}
		else if (key >= 59 && key <= 68)
			i = KeyEvent.VK_F1 + key - 59;
		else if (key >= 87 && key <= 88)
			i = KeyEvent.VK_F11 + key - 87;
		else if (key == 43)
			i = 130; // ^

		if (i >= 0)
		{
			listener.onControlKey(i, isDown);
			//			System.out.println("control key: " + i);
		}
		else
		{
			c = Character.toUpperCase(c);
			listener.onCharacterKey(c, isDown);

			if (c == 8)
				repeater.setKeyChar(key, c);
			//			System.out.println("character key: " + c + " (" + (int) c + ")");
		}
	}

	public void onSlopeChanged()
	{
		// slope has changed -> mouseInWorld has to be recalculated on next update
		mouseInt.x = -1;
	}
}
