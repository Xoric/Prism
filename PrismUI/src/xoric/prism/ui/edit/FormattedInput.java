package xoric.prism.ui.edit;

import xoric.prism.data.types.TextMap;

/**
 * @author XoricLee
 * @since 19.09.2011, 12:28:47
 */
public abstract class FormattedInput
{
	protected StringBuffer input = new StringBuffer();
	private int maxLength;
	private int selStart = 0;
	private int selLength = 0;

	public FormattedInput(int maxLength)
	{
		this.maxLength = maxLength;
	}

	public String substring(int start, int end)
	{
		return input.substring(start, end);
	}

	public String substring(int start)
	{
		return input.substring(start);
	}

	public void setInput(String s)
	{
		input = new StringBuffer(s);
		setSelection(selStart, selLength);
		makeInputValid();
	}

	public char charAt(int index)
	{
		return input.charAt(index);
	}

	public void setInput(int selStart, int selLength, String s)
	{
		input = new StringBuffer(s);
		setSelection(selStart, selLength);
		makeInputValid();
	}

	public int length()
	{
		return input.length();
	}

	@Override
	public String toString()
	{
		return input.toString();
	}

	public void selectAll()
	{
		setSelection(0, input.length());
	}

	public void setSelection(int start, int length)
	{
		final int inputLen = input.length();

		if (start < 0)
		{
			length += start;
			start = 0;
		}
		else if (start > inputLen)
		{
			start = inputLen;
			length = 0;
		}

		int availableLength = inputLen - start;
		if (length > availableLength)
			length = availableLength;
		if (length < 0)
			length = 0;

		this.selStart = start;
		this.selLength = length;
	}

	public int getSelectionStart()
	{
		return selStart;
	}

	public int getSelectionLength()
	{
		return selLength;
	}

	public void setMaxLength(int max)
	{
		this.maxLength = max;
		ensureMaxLength();
	}

	public int getMaxLength()
	{
		return maxLength;
	}

	private void ensureMaxLength()
	{
		if (maxLength > 0)
		{
			while (input.length() > maxLength)
				eraseChar(maxLength); // TODO improve performance?
		}
	}

	private void eraseInvalidChars()
	{
		int i = 0;
		boolean b;
		char c;

		while (i < input.length())
		{
			c = input.charAt(i);
			b = checkCharValid(c);
			if (!b)
				eraseChar(i);
			else
				++i;
		}
	}

	protected void eraseChar(int index)
	{
		input.deleteCharAt(index);

		if (index < selStart)
			selStart--;
		else if (index < selStart + selLength)
			selLength--;
	}

	private void makeInputValid()
	{
		eraseInvalidChars();
		extended();
		ensureMaxLength();
	}

	/**
	 * Invoke this in method {@link extended} if needed.
	 */
	protected void eraseDoubleSpace()
	{
		boolean lastSpace = true;
		boolean isSpace;
		int i = 0;
		char c;

		while (i < input.length())
		{
			c = input.charAt(i);
			isSpace = TextMap.isSpace(c);
			if (isSpace)
			{
				if (lastSpace)
					eraseChar(i);
				else
					++i;
			}
			else
				++i;
			lastSpace = isSpace;
		}
	}

	protected abstract void extended();

	protected abstract boolean checkCharValid(char c);
}
