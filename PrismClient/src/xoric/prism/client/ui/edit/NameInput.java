package xoric.prism.client.ui.edit;

import xoric.prism.data.types.TextMap;

/**
 * @author XoricLee
 * @since 19.09.2011, 19:25:25
 */
public class NameInput extends FormattedInput
{
	public NameInput(int maxLength)
	{
		super(maxLength);
	}

	private void eraseSuperfluousHyphens()
	{
		boolean hyphenFound = false;
		boolean isHyphen;
		int i = 0;
		char c;

		while (i < input.length())
		{
			c = input.charAt(i);
			isHyphen = TextMap.isHyphen(c);
			if (isHyphen)
			{
				if (hyphenFound)
				{
					eraseChar(i);
					--i;
				}
				else
					hyphenFound = true;
			}
			++i;
		}
	}

	@Override
	protected void extended()
	{
		eraseSuperfluousHyphens();
		// eraseDoubleSpace() - not needed since space is entirely forbidden
	}

	@Override
	protected boolean checkCharValid(char c)
	{
		boolean valid = TextMap.isLetter(c) || TextMap.isHyphen(c);
		return valid;
	}
}
