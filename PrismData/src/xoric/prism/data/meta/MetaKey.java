package xoric.prism.data.meta;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public enum MetaKey
{
	// do not resort
	ATTACH,
	TARGET,
	ITEM,
	SUB,
	ALT,
	COUNT,
	SIZE,
	IMPORT,
	TEXT;

	private static final MetaKey[] VALUES = values();

	public static MetaKey valueOf(int index) throws PrismException
	{
		if (index < 0 || index >= VALUES.length)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("error while converting int (" + index + ") to " + MetaKey.class.getSimpleName());
			// ----
			throw e;
		}
		return VALUES[index];
	}
}
