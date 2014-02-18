package xoric.prism.client.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public enum UIIdentifier
{
	WINDOW, FRAME, BUTTON, LABEL, EDIT;

	private static final UIIdentifier[] VALUES = values();

	public static UIIdentifier valueOf(int index) throws PrismException
	{
		if (index < 0 || index >= VALUES.length)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("error while converting int (" + index + ") to " + UIIdentifier.class.getSimpleName());
			// ----
			throw e;
		}
		return VALUES[index];
	}
}
