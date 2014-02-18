package xoric.prism.client.ui;

import xoric.prism.client.ui.button.UIButton;
import xoric.prism.client.ui.edit.UIEdit;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.types.IFloatPoint_r;

public abstract class UIFactory
{
	@Deprecated
	public static void appendComponent(Heap_out h, UIComponent c)
	{
		h.ints.add(c.getIdentifier().ordinal());
		c.appendTo(h);
	}

	public static MetaLine_out saveComponent(UIComponent c) throws PrismException
	{
		MetaLine_out ml = new MetaLine_out(MetaKey.ITEM);

		Heap_out h = ml.getHeap();
		h.ints.add(c.getIdentifier().ordinal());
		c.appendTo(h);

		return ml;
	}

	public static UIComponent createComponent(UIIdentifier id, IFloatPoint_r screenSize, IUIButtonHost buttonHost) throws PrismException
	{
		UIComponent c;

		switch (id)
		{
			case WINDOW:
				c = new UIWindow(screenSize);
				break;

			case FRAME:
				c = new UIFrame();
				break;

			case BUTTON:
				c = new UIButton(buttonHost);
				break;

			case LABEL:
				c = new UILabel();
				break;

			case EDIT:
				c = new UIEdit();
				break;

			default:
				PrismException e = new PrismException();
				// ----
				e.user.setText(UserErrorText.INTERNAL_PROBLEM);
				// ----
				e.code.setText("extracting " + id.toString() + " not implemented");
				// ----
				throw e;
		}
		return c;
	}

	public static UIComponent loadComponent(UIIdentifier id, Heap_in h, IFloatPoint_r screenSize, IUIButtonHost buttonHost)
			throws PrismException
	{
		// create component
		UIComponent c = createComponent(id, screenSize, buttonHost);

		// extract parameters
		c.extractFrom(h);

		return c;
	}

	@Deprecated
	public static UIComponent loadComponent(MetaLine_in ml, IFloatPoint_r screenSize, IUIButtonHost buttonHost) throws PrismException
	{
		Heap_in h = ml.getHeap();

		// extract identifier
		int v = h.nextInt();
		UIIdentifier id = UIIdentifier.valueOf(v);

		// create component
		UIComponent c = createComponent(id, screenSize, buttonHost);

		// extract parameters
		c.extractFrom(h);

		return c;
	}

	@Deprecated
	public static UIComponent extractComponent(Heap_in h, IUIButtonHost buttonHost) throws PrismException
	{
		UIComponent c;

		// extract identifier
		int v = h.nextInt();
		UIIdentifier id = UIIdentifier.valueOf(v);

		// create component
		switch (id)
		{
			case FRAME:
				c = new UIFrame();
				break;

			case BUTTON:
				c = new UIButton(buttonHost);
				break;

			case LABEL:
				c = new UILabel();
				break;

			default:
				PrismException e = new PrismException();
				// ----
				e.user.setText(UserErrorText.INTERNAL_PROBLEM);
				// ----
				e.code.setText("extracting " + id.toString() + " not implemented");
				// ----
				throw e;
		}

		// extract parameters
		c.extractFrom(h);

		return c;
	}
}
