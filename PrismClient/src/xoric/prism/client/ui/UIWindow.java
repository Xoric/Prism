package xoric.prism.client.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.client.ui.actions.ButtonAction;
import xoric.prism.client.ui.actions.ButtonActionIndex;
import xoric.prism.client.ui.actions.IActionHandler;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;

public class UIWindow extends UIFrame implements IUIButtonHost
{
	private static final Text DEFAULT_TITLE = new Text("WINDOW");

	private IActionHandler actionHandler;

	private IFloatPoint_r screenSize;
	private static final float SAFETY = 30.0f;

	private UIButton closeButton;
	private final List<UIComponent> components;

	public UIWindow(IFloatPoint_r screenSize)
	{
		components = new ArrayList<UIComponent>();
		//		this.screenSize = screenSize;
		setText(DEFAULT_TITLE);

		//		UIButton b = new UIButton(this);
		//		b.setText(new Text("MY BUTTON"));
		//		b.setPosition(20.0f, 40.0f);
		//		b.setWidth(120.0f);
		//
		//		addComponent(b);

		addCloseButton();
	}

	public void setScreenSize(IFloatPoint_r screenSize)
	{
		this.screenSize = screenSize;
	}

	public void addComponent(UIComponent c)
	{
		registerChild(c);
		components.add(c);
	}

	public List<UIComponent> getComponents()
	{
		return components;
	}

	public void addCloseButton()
	{
		if (closeButton == null)
		{
			closeButton = new UIButton(this);
			closeButton.setActionIndex(ButtonActionIndex.CLOSE_WINDOW);
			closeButton.setXRuler(-23.0f, 1.0f);
			closeButton.setYRuler(0.5f, 0.0f);
			closeButton.setWidthRuler(23.0f, 0.0f);
			closeButton.setText(new Text("X"));

			addComponent(closeButton);
		}
	}

	public void setActionHandler(IActionHandler actionHandler)
	{
		this.actionHandler = actionHandler;
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		super.draw(renderer);

		for (UIComponent c : components)
			c.draw(renderer);
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
			m = screenSize.getX() - SAFETY;
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
			m = screenSize.getY() - SAFETY;
			d = m - y;
			if (d < 0.0f)
				dy += d;

		}

		// call parent
		super.moveBy(dx, dy);
	}
}
