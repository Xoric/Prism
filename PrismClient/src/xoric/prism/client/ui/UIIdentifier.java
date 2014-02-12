package xoric.prism.client.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public enum UIIdentifier
{
	FRAME, BUTTON, LABEL;

	private static final UIIdentifier[] VALUES = values();

	public static UIIdentifier valueOf(int v) throws PrismException
	{
		if (v < 0 || v >= VALUES.length)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("error while converting int to " + UIIdentifier.class.toString());
			e.code.addInfo("value", v);
			e.code.addInfo("enum count", VALUES.length);
			// ----
			throw e;
		}
		return VALUES[v];
	}
}
