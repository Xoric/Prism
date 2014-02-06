package xoric.prism.scene.lwjgl;

import org.lwjgl.input.Mouse;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.Point;
import xoric.prism.scene.IInputListener;

public class InputHandlerLWJGL
{
	private IInputListener listener;
	private int windowHeight;

	private final Point mouseInt;
	private final FloatPoint mouse;

	private boolean isLeftButtonDown;
	private boolean isRightButtonDown;

	public InputHandlerLWJGL()
	{
		mouseInt = new Point();
		mouse = new FloatPoint();
	}

	public void initialize(IInputListener listener, int windowHeight)
	{
		this.listener = listener;
		this.windowHeight = windowHeight;
	}

	public void update()
	{
		// get mouse coordinates
		int x = Mouse.getX();
		int y = windowHeight - Mouse.getY();

		if (x != mouse.x || y != mouse.y)
		{
			mouse.x = x;
			mouse.y = y;
			mouseInt.x = x;
			mouseInt.y = y;

			listener.mouseMove(mouse);
		}

		// check left mouse button
		boolean b = Mouse.isButtonDown(0);
		// --
		if (isLeftButtonDown != b)
		{
			if (b)
				listener.onMouseDown(mouse, true);
			else
				listener.onMouseUp(mouse, true);

			isLeftButtonDown = b;
		}

		// check right mouse button
		b = Mouse.isButtonDown(1);
		// --
		if (isRightButtonDown != b)
		{
			if (b)
				listener.onMouseDown(mouse, false);
			else
				listener.onMouseUp(mouse, false);

			isRightButtonDown = b;
		}

		/*
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_X); // is x key down.
		while (Keyboard.next())
		{
			if (Keyboard.getEventKeyState())
			{
				if (Keyboard.getEventKey() == Keyboard.KEY_A)
				{
					System.out.println("A Key Pressed");
				}
			}
			else
			{
				if (Keyboard.getEventKey() == Keyboard.KEY_A)
				{
					System.out.println("A Key Released");
				}
			}
		}
		*/
	}
}
