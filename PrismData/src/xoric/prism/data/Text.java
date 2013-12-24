package xoric.prism.data;

import java.awt.event.KeyEvent;

/**
 * @author Felix M�hrle
 * @since 26.05.2011, 13:47:31
 */
public class Text
{
	protected final StringBuffer ascii = new StringBuffer();
	protected final StringBuffer text = new StringBuffer();

	/**
	 * Text constructor.
	 * @param s
	 */
	public Text(String s)
	{
		set(s);
	}

	public Text()
	{
		set("");
	}

	public String cut(int count)
	{
		if (count < 1)
			count = 1;

		String s = ascii.toString();
		if (s.length() > count)
			s = s.substring(0, count - 1) + '�';

		return s;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean b;
		if (obj instanceof Text)
		{
			Text other = (Text) obj;
			b = other.toString().equals(this.toString());
		}
		else
			b = false;
		return b;
	}

	/**
	 * Sets the text.
	 * @param s
	 */
	public void set(String s)
	{
		char c;
		boolean lastCharWasSpace = false; // text may begin with a SPACE
		boolean isSpace;
		clear();

		if (s != null)
		{
			for (int i = 0; i < s.length(); ++i)
			{
				c = s.charAt(i);
				isSpace = (c == KeyEvent.VK_SPACE);

				if (!isSpace || !lastCharWasSpace)
					append(c);

				lastCharWasSpace = isSpace;
			}
		}
	}

	/**
	 * Sets length to {@code zero}.
	 */
	public void clear()
	{
		ascii.setLength(0);
		text.setLength(0);
	}

	/**
	 * @param index
	 * @return byte
	 */
	public byte indexAt(int index)
	{
		return (byte) text.charAt(index);
	}

	/**
	 * @param beginIndex
	 * @param endIndex
	 * @return Substring
	 */
	public String substring(int beginIndex, int endIndex)
	{
		return toString().substring(beginIndex, endIndex);
	}

	/**
	 * @param beginIndex
	 * @return Substring
	 */
	public String substring(int beginIndex)
	{
		return toString().substring(beginIndex);
	}

	/**
	 * Returns the ascii character at the given position.
	 * @param index
	 * @return Character
	 */
	public char charAt(int index)
	{
		return ascii.charAt(index);
	}

	@Override
	public String toString()
	{
		return ascii.toString();
	}

	/**
	 * Returns the length.
	 * @return int
	 */
	public int length()
	{
		return ascii.length();
	}

	/**
	 * Tries to append a character.
	 * @param c
	 *            character to append
	 */
	public void append(char c)
	{
		int i = TextMap.indexOf(c, -1);
		if (i >= 0)
		{
			text.append((char) i);
			ascii.append(TextMap.charOf(i));
		}
	}
}
