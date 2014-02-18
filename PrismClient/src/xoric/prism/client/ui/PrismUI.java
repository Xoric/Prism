package xoric.prism.client.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.client.ui.button.IActionExecuter;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;

public class PrismUI implements IDrawableUI
{
	//	private final IActionParent client;
	private final IActionExecuter actionExecuter;
	private final FloatRect screenRect;

	private final List<UIWindow> windows;
	private UIWindow activeComponentWindow;
	private IActiveUI activeComponent;
	private final FloatPoint last;

	private boolean isMouseDown;

	public PrismUI(IActionExecuter actionExecuter)
	{
		this.actionExecuter = actionExecuter;
		this.screenRect = new FloatRect();
		this.windows = new ArrayList<UIWindow>();
		this.last = new FloatPoint();
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

	public IActiveUI mouseDown(IFloatPoint_r mouse)
	{
		isMouseDown = true;
		IActiveUI ac = null;

		for (int i = windows.size() - 1; i >= 0; --i)
		{
			UIWindow w = windows.get(i);
			ac = w.mouseDown(mouse);

			if (activeComponent != null)
				activeComponent.setFocus(false);

			if (ac != null)
			{
				activeComponentWindow = w;
				activeComponent = ac;
				activeComponent.setFocus(true);
				sendToFront(activeComponentWindow);
				return ac;
			}
		}
		releaseActiveComponent();
		return null;
	}

	private void releaseActiveComponent()
	{
		if (activeComponent != null)
		{
			activeComponentWindow = null;
			activeComponent = null;
		}
	}

	public void mouseUp(IFloatPoint_r mouse) throws PrismException
	{
		isMouseDown = false;

		if (activeComponent != null)
		{
			if (activeComponent.containsMouse(mouse))
				activeComponent.mouseClick();

			if (activeComponent != null)
				activeComponent.mouseUp();

			//			activeComponentWindow = null;
			//			activeComponent = null;
		}
	}

	public void mouseMove(IFloatPoint_r mouse)
	{
		if (isMouseDown && activeComponent instanceof UIWindow)
		{
			UIWindow w = (UIWindow) activeComponent;
			float dx = mouse.getX() - last.x;
			float dy = mouse.getY() - last.y;

			if (w.isResizing())
				w.resize(dx, dy);
			else
				w.moveBy(dx, dy);
		}
		last.x = mouse.getX();
		last.y = mouse.getY();
	}

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

	public void onControlKey(int keyCode, boolean isDown)
	{
		if (activeComponent != null)
		{
			activeComponent.onControlKey(keyCode, isDown);
		}
	}

	public void onCharacterKey(char c, boolean isDown)
	{
		if (activeComponent != null)
		{
			activeComponent.onCharacterKey(c, isDown);
		}
	}
}
