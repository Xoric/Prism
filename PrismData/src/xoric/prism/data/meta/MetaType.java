package xoric.prism.data.meta;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;

public enum MetaType
{
	COMMON,
	TOC /* file table */,
	SHADER,
	DEVELOP /* contains attachments and targetFile */,
	ANIM_D /* animation-names for ModelCreator etc. */,
	MODEL_G,
	URGENCY, /* message urgency */
	COLLECTION, /* sprite collection */
	GRID, /* sprite grid */
	WINDOW, /* UIWindow */
	MAP, /* world map */
	GROUND; /* ground types */
	// max index: 255

	private static final MetaType[] VALUES = values();

	public static MetaType valueOf(int index) throws PrismException
	{
		if (index < 0 || index >= VALUES.length)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("error while converting int (" + index + ") to " + MetaType.class.getSimpleName());
			// ----
			throw e;
		}
		return VALUES[index];
	}
}
