package xoric.prism.creator.windows.view.tree;

import xoric.prism.ui.edit.InputFormat;

/**
 * @author XoricLee
 * @since 18.02.2014, 10:02:01
 */
class InputFormatResult
{
	public final InputFormat format;
	public final int maxLength;

	public InputFormatResult(InputFormat format, int maxLength)
	{
		this.format = format;
		this.maxLength = maxLength;
	}
}
