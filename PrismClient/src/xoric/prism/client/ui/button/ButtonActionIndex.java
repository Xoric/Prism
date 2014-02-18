package xoric.prism.client.ui.button;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public enum ButtonActionIndex
{
	NONE, EXIT_GAME, LOGIN;
	// unlimited

	public static final ButtonActionIndex[] VALUES = values();
	public static final int COUNT = VALUES.length;

	public static ButtonActionIndex valueOf(int v) throws PrismException
	{
		if (v < 0 || v >= VALUES.length)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("invalid conversion from int " + v + " to " + ButtonActionIndex.class.getSimpleName());
			e.code.addInfo("enum count", VALUES.length);
			throw e;
		}
		return VALUES[v];
	}
}
