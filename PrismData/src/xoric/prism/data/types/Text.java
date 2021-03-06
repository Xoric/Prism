package xoric.prism.data.types;

import java.awt.event.KeyEvent;

/**
 * @author Felix M�hrle
 * @since 26.05.2011, 13:47:31
 */
public class Text implements IText_r // TODO needs a rework
{
	public static final IText_r EMPTY = new Text("");

	protected final StringBuilder ascii = new StringBuilder();
	protected final StringBuilder symbols = new StringBuilder(); // TODO change to List<Byte>

	/**
	 * Text constructor.
	 * @param s
	 */
	public Text(String s)
	{
		set(s);
	}

	public Text(IText_r t)
	{
		set(t == null ? "" : t.toString()); // TODO copy ascii and symbols directly
	}

	public Text()
	{
		set("");
	}

	@Override
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
		if (obj instanceof IText_r)
		{
			IText_r other = (IText_r) obj;
			b = other.toString().equals(this.toString());
		}
		else if (obj instanceof String)
		{
			String other = (String) obj;
			b = other.equals(this.toString());
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

	public void set(IText_r name)
	{
		set(name.toString()); // TODO copy ascii and symbols directly
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

	@Override
	public int findSeparator(int startIndex)
	{
		for (int i = startIndex; i < ascii.length(); ++i)
			if (TextMap.isSeparator(ascii.charAt(i)))
				return i;

		return -1;
	}

	public Text subtext(int lastIndex, int index)
	{
		return new Text(substring(lastIndex, index)); // TODO reimplement 
	}

	public void fill(int len, char c)
	{
		set("");
		for (int i = 0; i < len; ++i)
			append(c);
	}
}
