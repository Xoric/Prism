package xoric.prism.ui.edit;

import xoric.prism.data.types.TextMap;

/**
 * @author XoricLee
 * @since 19.09.2011, 13:06:43
 */
public class DefaultInput extends FormattedInput
{
	public DefaultInput(int maxLength)
	{
		super(maxLength);
	}

	@Override
	protected boolean checkCharValid(char c)
	{
		return TextMap.isValid(c);
	}

	@Override
	protected void extended()
	{
		eraseDoubleSpace();
	}
}
