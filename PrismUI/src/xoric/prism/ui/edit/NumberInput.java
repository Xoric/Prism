package xoric.prism.ui.edit;

import xoric.prism.data.types.TextMap;

/**
 * @author XoricLee
 * @since 19.09.2011, 14:52:42
 */
public class NumberInput extends FormattedInput
{
	public NumberInput(int maxLength)
	{
		super(maxLength);
	}

	private void eraseLeadingZeros()
	{
		int i = 0;
		char c;

		while (i < input.length())
		{
			c = input.charAt(i);
			if (c == '0')
				eraseChar(i);
			else
				return;
		}
	}

	@Override
	protected void extended()
	{
		eraseLeadingZeros();
	}

	@Override
	protected boolean checkCharValid(char c)
	{
		return TextMap.isNumber(c);
	}
}
