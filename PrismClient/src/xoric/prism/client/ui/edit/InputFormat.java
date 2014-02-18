package xoric.prism.client.ui.edit;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

/**
 * @author XoricLee
 * @since 19.09.2011, 00:56:29
 */
public enum InputFormat
{
	NORMAL, PASSWORD, NUMBERS, ACC_NAME, CHAR_NAME;

	public static final InputFormat[] VALUES = values();
	public static final int COUNT = VALUES.length;

	public static InputFormat valueOf(int index) throws PrismException
	{
		if (index < 0 || index >= VALUES.length)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("error while converting int (" + index + ") to " + InputFormat.class.getSimpleName());
			// ----
			throw e;
		}
		return VALUES[index];
	}
}
