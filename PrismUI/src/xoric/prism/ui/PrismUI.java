package xoric.prism.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.IExceptionSink;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IInputListener;
import xoric.prism.scene.IRendererUI;
import xoric.prism.ui.action.IActionExecuter;

public class PrismUI implements IDrawableUI, IInputListener
{
	private final IExceptionSink exceptionSink;

	//	private final IActionParent client;
	private final IActionExecuter actionExecuter;
	private final FloatRect screenRect;

	private final List<UIWindow> windows;
	private UIWindow activeComponentWindow;
	private IActiveUI activeComponent;
	private final FloatPoint newestMouse;
	private final FloatPoint lastMouse;

	private boolean isMouseDown;

	public PrismUI(IExceptionSink exceptionSink, IActionExecuter actionExecuter)
	{
		this.exceptionSink = exceptionSink;
		this.actionExecuter = actionExecuter;
		this.screenRect = new FloatRect();
		this.windows = new ArrayList<UIWindow>();
		this.newestMouse = new FloatPoint();
		this.lastMouse = new FloatPoint();
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		for (UIWindow w : windows)
			w.draw(renderer);
	}

	public void addWindow(UIWindow w)
	{
		w.setActionExecuter(actionExecuter);
		w.setScreenSize(screenRect.getSize());
		w.rearrange(screenRect);
		windows.add(w);
	}

	private void sendToFront(UIWindow w)
	{
		int j = windows.indexOf(w);

		if (j >= 0)
		{
			windows.remove(j);
			windows.add(w);
		}
	}

	private void releaseActiveComponent()
	{
		if (activeComponent != null)
		{
			activeComponentWindow = null;
			activeComponent = null;
		}
	}

	//	public IActiveUI mouseDown(IFloatPoint_r mouse)
	//	{
	//		isMouseDown = true;
	//		IActiveUI ac = null;
	//
	//		for (int i = windows.size() - 1; i >= 0; --i)
	//		{
	//			UIWindow w = windows.get(i);
	//			ac = w.mouseDown(mouse);
	//
	//			if (activeComponent != null)
	//				activeComponent.setFocus(false);
	//
	//			if (ac != null)
	//			{
	//				activeComponentWindow = w;
	//				activeComponent = ac;
	//				activeComponent.setFocus(true);
	//				sendToFront(activeComponentWindow);
	//				return ac;
	//			}
	//		}
	//		releaseActiveComponent();
	//		return null;
	//	}

	//	public void mouseUp(IFloatPoint_r mouse) throws PrismException
	//	{
	//		isMouseDown = false;
	//
	//		if (activeComponent != null)
	//		{
	//			if (activeComponent.containsMouse(mouse))
	//				activeComponent.mouseClick();
	//
	//			if (activeComponent != null)
	//				activeComponent.mouseUp();
	//
	//			//			activeComponentWindow = null;
	//			//			activeComponent = null;
	//		}
	//	}

	public void closeWindow(UIWindow w)
	{
		int i = windows.indexOf(w);

		if (i >= 0)
		{
			if (w == activeComponentWindow)
				releaseActiveComponent();

			windows.remove(i);
		}
	}

	public void setScreenSize(IFloatPoint_r screenSize)
	{
		screenRect.setSize(screenSize);
	}

	// IInputListener:
	@Override
	public boolean onControlKey(int keyCode, boolean isDown)
	{
		if (activeComponent != null)
		{
			activeComponent.onControlKey(keyCode, isDown);
			return true;
		}
		return false;
	}

	// IInputListener:
	@Override
	public boolean onCharacterKey(char c, boolean isDown)
	{
		if (activeComponent != null)
		{
			activeComponent.onCharacterKey(c, isDown);
			return true;
		}
		return false;
	}

	// IInputListener:
	@Override
	public boolean onMouseDown(IFloatPoint_r mouse, boolean isLeft)
	{
		isMouseDown = true;
		IActiveUI ac = null;
		newestMouse.copyFrom(mouse);
		newestMouse.multiply(screenRect.getSize());

		for (int i = windows.size() - 1; i >= 0; --i)
		{
			UIWindow w = windows.get(i);
			ac = w.mouseDown(newestMouse);

			if (activeComponent != null)
				activeComponent.setFocus(false);

			if (ac != null)
			{
				activeComponentWindow = w;
				activeComponent = ac;
				activeComponent.setFocus(true);
				sendToFront(activeComponentWindow);
				return true;
			}
		}
		releaseActiveComponent();
		return false;
	}

	// IInputListener:
	@Override
	public void onMouseMove(IFloatPoint_r mouse)
	{
		newestMouse.copyFrom(mouse);
		newestMouse.multiply(screenRect.getSize());

		if (isMouseDown && activeComponent instanceof UIWindow && ((UIWindow) activeComponent).isMoveable())
		{
			UIWindow w = (UIWindow) activeComponent;
			float dx = newestMouse.x - lastMouse.x;
			float dy = newestMouse.y - lastMouse.y;

			if (w.isResizing())
				w.resize(dx, dy);
			else
				w.moveBy(dx, dy);
		}
		lastMouse.copyFrom(newestMouse);
	}

	// IInputListener:
	@Override
	public void onMouseUp(IFloatPoint_r mouse, boolean isLeft)
	{
		isMouseDown = false;

		if (activeComponent != null)
		{
			newestMouse.copyFrom(mouse);
			newestMouse.multiply(screenRect.getSize());

			if (activeComponent.containsMouse(newestMouse))
			{
				try
				{
					activeComponent.mouseClick();
				}
				catch (PrismException e)
				{
					exceptionSink.receiveException(e);
				}
			}

			if (activeComponent != null)
				activeComponent.mouseUp();
		}
	}
}
