package xoric.prism.client.ui.actions;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public enum ButtonActionIndex
{
	NONE, CLOSE_WINDOW;

	private static final ButtonActionIndex[] VALUES = values();

	public static ButtonActionIndex valueOf(int v) throws PrismException
	{
		if (v < 0 || v >= VALUES.length)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("invalid conversion from int " + v + " to " + ButtonActionIndex.class.toString());
			e.code.addInfo("enum count", VALUES.length);
			throw e;
		}
		return VALUES[v];
	}
}
