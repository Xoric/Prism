package xoric.prism.client.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.packable.IntPacker;

public abstract class UIFactory
{
	public static void packComponent(OutputStream stream, UIComponent c) throws IOException, PrismException
	{
		UIIdentifier id = c.getIdentifier();
		IntPacker.pack_s(stream, id.ordinal());
		c.pack(stream);
	}

	public static UIComponent unpackComponent(InputStream stream, IUIButtonHost buttonHost) throws IOException, PrismException
	{
		UIComponent c;

		int v = IntPacker.unpack_s(stream);
		UIIdentifier id = UIIdentifier.valueOf(v);

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
				e.code.setText(UIIdentifier.class.toString() + " not implemented");
				e.code.addInfo("identifier", id.toString());
				e.code.addInfo("operation", "unpack " + UIComponent.class.toString() + " in " + UIFactory.class.toString());
				// ----
				throw e;
		}
		c.unpack(stream);

		return c;
	}
}
