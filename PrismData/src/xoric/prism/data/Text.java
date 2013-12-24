package xoric.prism.data;

import java.awt.event.KeyEvent;

/**
 * @author Felix Möhrle
 * @since 26.05.2011, 13:47:31
 */
public class Text implements IText_r
{
	protected final StringBuffer ascii = new StringBuffer();
	protected final StringBuffer symbols = new StringBuffer(); // TODO change to List<Byte>

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
			s = s.substring(0, count - 1) + '…';

		return s;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean b;
		if (obj instanceof IText_r)
		{
			IText_r other = (IText_r) obj;
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
		symbols.setLength(0);
	}

	@Override
	public byte symbolAt(int index)
	{
		return (byte) symbols.charAt(index);
	}

	@Override
	public String substring(int beginIndex, int endIndex)
	{
		return toString().substring(beginIndex, endIndex);
	}

	@Override
	public String substring(int beginIndex)
	{
		return toString().substring(beginIndex);
	}

	@Override
	public char charAt(int index)
	{
		return ascii.charAt(index);
	}

	@Override
	public String toString()
	{
		return ascii.toString();
	}

	@Override
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
		int i = TextMap.symbolOf(c, -1);
		if (i >= 0)
		{
			symbols.append((char) i);
			ascii.append(TextMap.charOf(i));
		}
	}
}
