package xoric.prism.client.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.client.ui.actions.ButtonAction;
import xoric.prism.client.ui.actions.ButtonActionIndex;
import xoric.prism.client.ui.actions.IActionHandler;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

public class UIWindow extends UIFrame implements IUIButtonHost
{
	private static final Text DEFAULT_TITLE = new Text("WINDOW");

	private IActionHandler actionHandler;

	private final FloatRect screenRect;
	//	private IFloatPoint_r screenSize;
	private static final float SAFETY = 30.0f;

	private UIButton closeButton;
	//	private FloatPoint cornerPos;
	//	private FloatPoint cornerOnset;
	private FloatRect cornerRect;
	private boolean isResizing;
	private final List<UIComponent> components;

	public UIWindow(IFloatPoint_r screenSize)
	{
		components = new ArrayList<UIComponent>();

		screenRect = new FloatRect();

		//		this.screenSize = screenSize;
		setText(DEFAULT_TITLE);

		//		UIButton b = new UIButton(this);
		//		b.setText(new Text("MY BUTTON"));
		//		b.setPosition(20.0f, 40.0f);
		//		b.setWidth(120.0f);
		//
		//		addComponent(b);

		//		makeResizable(true);
		//		makeClosable(true);
	}

	public void makeResizable(boolean b)
	{
		if (b && cornerRect == null)
		{
			cornerRect = new FloatRect();
			cornerRect.setSize(Materials.frames.getMeta().getObject(3).getSize());

			rearrangeCorner();
		}
		else if (!b && cornerRect != null)
		{
			cornerRect = null;
		}
	}

	public boolean isResizing()
	{
		return isResizing;
	}

	public void setScreenSize(IFloatPoint_r screenSize)
	{
		screenRect.setSize(screenSize);
		//		this.screenSize = screenSize;
	}

	public List<UIComponent> getComponents()
	{
		return components;
	}

	public void makeClosable(boolean b)
	{
		if (b && closeButton == null)
		{
			closeButton = new UIButton(this);
			closeButton.setActionIndex(ButtonActionIndex.CLOSE_WINDOW);
			closeButton.setXRuler(-26.0f, 1.0f);
			closeButton.setYRuler(0.5f, 0.0f);
			closeButton.setWidthRuler(26.0f, 0.0f);
			closeButton.setText(new Text("X"));
			addComponent(closeButton);
		}
		else if (!b && closeButton != null)
		{
			unregisterChild(closeButton);
			components.remove(closeButton);
			closeButton = null;
		}
	}

	public void addComponent(UIComponent c)
	{
		registerChild(c);
		components.add(c);
	}

	public void setActionHandler(IActionHandler actionHandler)
	{
		this.actionHandler = actionHandler;
	}

	@Override
	protected IActiveUI mouseDownConfirmed(IFloatPoint_r mouse)
	{
		for (UIComponent c : components)
		{
			IActiveUI a = c.mouseDown(mouse);
			if (a != null)
				return a;
		}

		isResizing = false;

		if (cornerRect != null)
		{
			if (cornerRect.contains(mouse))
			{
				float z = (mouse.getX() - cornerRect.getX()) / (cornerRect.getWidth());
				isResizing = (mouse.getY() > z * cornerRect.getY() + (1.0f - z) * cornerRect.getBottom());
			}
		}
		return this;
	}

	private void validateComponentIndex(int componentIndex, String importType) throws PrismException
	{
		if (componentIndex < 0 || componentIndex > components.size())
		{
			IText_r title = getText();
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("importing " + importType + " from ui component failed");
			e.code.addInfo("component index", componentIndex);
			e.code.addInfo("component count", components.size());
			e.code.addInfo("window title", title == null ? "null" : title.toString());
			throw e;
		}
	}

	private void rejectInput(UIComponent c, int componentIndex, String importType) throws PrismException
	{
		IText_r title = getText();
		PrismException e = new PrismException();
		e.user.setText(UserErrorText.INTERNAL_PROBLEM);
		e.code.setText("component does not support import type " + importType);
		e.code.addInfo("component index", componentIndex);
		e.code.addInfo("component type", c.getClass().toString());
		e.code.addInfo("window title", title == null ? "null" : title.toString());
		throw e;
	}

	@Override
	public int importIntFrom(int componentIndex) throws PrismException
	{
		validateComponentIndex(componentIndex, "int");
		UIComponent c = components.get(componentIndex);
		int i = c.importInt();
		if (i < 0)
			rejectInput(c, componentIndex, "int");

		return i;
	}

	@Override
	public Text importTextFrom(int componentIndex) throws PrismException
	{
		validateComponentIndex(componentIndex, "Text");
		UIComponent c = components.get(componentIndex);
		Text t = c.importText();
		if (t == null)
			rejectInput(c, componentIndex, "Text");

		return t;
	}

	@Override
	public void executeAction(ButtonAction a)
	{
		actionHandler.executeAction(this, a);
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		super.draw(renderer);

		if (cornerRect != null)
		{
			Materials.framesDrawer.setup(0, 3, 0);
			Materials.framesDrawer.drawSingle(cornerRect.getTopLeft());
		}

		for (UIComponent c : components)
			c.draw(renderer);
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		super.rearrange(parentRect);

		rearrangeCorner();
	}

	private void rearrangeCorner()
	{
		if (cornerRect != null)
			cornerRect.setBottomRight(rect.getBottomRight());
	}

	public void resize(float dx, float dy)
	{
		rect.addSize(dx, dy);

		if (rect.getWidth() < 100.0f)
			rect.setWidth(100.0f);

		if (rect.getHeight() < 35.0f)
			rect.setHeight(35.0f);

		// TODO implement an upper limit for window size

		rearrangeCorner();
		rearrangeChildrenOnly();
	}

	@Override
	public void moveBy(float dx, float dy)
	{
		// limit left
		float x = rect.getX() + dx;
		float m = SAFETY - rect.getWidth();
		float d = m - x;
		//
		if (d > 0.0f)
		{
			dx += d;
		}
		else
		{
			// limit right
			m = screenRect.getWidth() - SAFETY;
			d = m - x;
			if (d < 0.0f)
				dx += d;
		}

		// limit top
		float y = rect.getY() + dy;
		m = SAFETY - rect.getHeight();
		d = m - y;
		if (d > 0.0f)
		{
			dy += d;
		}
		else
		{
			// limit bottom
			m = screenRect.getHeight() - SAFETY;
			d = m - y;
			if (d < 0.0f)
				dy += d;
		}

		// move own stuff
		if (cornerRect != null)
			cornerRect.addPosition(dx, dy);

		// call parent
		super.moveBy(dx, dy);
	}

	public boolean isResizable()
	{
		return cornerRect != null;
	}

	public boolean isClosable()
	{
		return closeButton != null;
	}
}
