package xoric.prism.client.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IText_r;

public enum ClientActionCommand
{
	NONE, EXIT_GAME, LOGIN, CREATE_ACC;
	// unlimited

	public static final ClientActionCommand[] VALUES = values();
	public static final int COUNT = VALUES.length;

	public static ClientActionCommand valueOf(IText_r text)
	{
		ClientActionCommand c = text == null ? NONE : valueOf(text.toString());
		return c;
	}

	public static ClientActionCommand valueOf(int v) throws PrismException
	{
		if (v < 0 || v >= VALUES.length)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("invalid conversion from int " + v + " to " + ClientActionCommand.class.getSimpleName());
			e.code.addInfo("enum count", VALUES.length);
			throw e;
		}
		return VALUES[v];
	}
}
