package xoric.prism.creator.windows.scene;

import xoric.prism.client.ui.UIComponent;
import xoric.prism.client.ui.UIComponentH;
import xoric.prism.client.ui.UIFactory;
import xoric.prism.client.ui.UIIdentifier;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;

public class AddComponentAction extends SceneAction
{
	private static final float defaultWidth = 80.0f;

	private final UIIdentifier identifier;

	public AddComponentAction(UIIdentifier identifier)
	{
		super("ADD " + identifier.toString());

		this.identifier = identifier;
	}

	private void initComponent(UIComponent c, IFloatPoint_r mouseOnWindow)
	{
		int miny = 40;
		int y = ((int) mouseOnWindow.getY()) - miny;
		if (y < 0)
			y = 0;

		y /= 30;
		y *= 30;
		y += miny;

		c.setYRuler(y, 0.0f);

		if (c instanceof UIComponentH)
		{
			UIComponentH h = (UIComponentH) c;
			h.setHeightRuler(60.0f, 0.0f);
		}

		if (align == Align.LEFT)
		{
			c.setWidthRuler(defaultWidth, 0.0f);
			c.setXRuler(30.0f, 0.0f);
		}
		else if (align == Align.CENTER)
		{
			c.setWidthRuler(-60.0f, 1.0f);
			c.setXRuler(30.0f, 0.0f);
		}
		else if (align == Align.RIGHT)
		{
			c.setWidthRuler(defaultWidth, 0.0f);
			c.setXRuler(-30.0f - 0.5f * defaultWidth, 1.0f);
		}
	}

	@Override
	public boolean mouseMove(IFloatPoint_r mouseOnWindow)
	{
		return true;
	}

	@Override
	public boolean onMouseDown(IFloatPoint_r mouseOnScreen, IFloatPoint_r mouseOnWindow)
	{
		try
		{
			UIComponent c = UIFactory.createComponent(identifier, sceneRect.getSize(), null);
			initComponent(c, mouseOnWindow);
			control.requestAddComponent(c);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
		return true;
	}

	@Override
	public boolean onMouseUp(IFloatPoint_r mouseOnWindow)
	{
		return true;
	}
}
