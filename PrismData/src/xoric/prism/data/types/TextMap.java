/**
 * 
 */
package xoric.prism.data.types;

/**
 * @author Felix M�hrle
 * @since 26.05.2011, 13:50:56
 */
public abstract class TextMap
{
	private static int LESSER = 26;
	private static int EQUAL = 27;
	private static int GREATER = 28;
	private static int QUESTION_MARK = 29;
	private static int AT = 30;
	private static int SPACE = 31;
	private static int EXCLAMATION_MARK = 32;
	private static int ROOF = 33;
	private static int DOLLAR = 34;
	private static int PERCENT = 35;
	private static int QUOTE = 36;
	private static int OPEN_BRACKET = 37;
	private static int CLOSE_BRACKET = 38;
	private static int PRODUCT = 39;
	private static int PLUS = 40;
	private static int COMMA = 41;
	private static int MINUS = 42;
	private static int DOT = 43;
	private static int SLASH = 44;
	private static int ZERO = 45;
	private static int COLON = 55;
	private static int SEMICOLON = 56;
	private static int UNDERLINE = 57;
	private static int PARA = 58;
	private static int INVALID = -1;

	private static final int[] INDICES = new int[] { SPACE, EXCLAMATION_MARK, QUOTE, INVALID, DOLLAR, PERCENT, INVALID, QUOTE,
			OPEN_BRACKET, CLOSE_BRACKET, PRODUCT, PLUS, COMMA, MINUS, DOT, SLASH, ZERO, ZERO + 1, ZERO + 2, ZERO + 3, ZERO + 4, ZERO + 5,
			ZERO + 6, ZERO + 7, ZERO + 8, ZERO + 9, COLON, SEMICOLON, LESSER, EQUAL, GREATER, QUESTION_MARK, AT, PARA };

	/**
	 * Tries to convert a character to make it compatible to the internal alphabet.
	 * @param c
	 *            Character to adapt
	 * @return Compatible character or 0 if not possible
	 */
	public static String convertChar(char c)
	{
		String result;
		int symbol = symbolOf(c, TextMap.INVALID);

		if (symbol != TextMap.INVALID)
			result = String.valueOf(charOf(symbol));
		else
		{
			c = Character.toUpperCase(c);

			if ((c == '�') || (c == '�'))
				result = "AE";
			else if ((c == '�') || (c == '�'))
				result = "OE";
			else if ((c == '�') || (c == '�'))
				result = "UE";
			else if (c == '�')
				result = "SS";
			else if (c == '\\')
				result = "/";
			else
				result = "";
		}

		return result;
	}

	public static String convertString(String s)
	{
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); ++i)
			sb.append(convertChar(s.charAt(i)));

		return sb.toString();
	}

	/**
	 * Converts an index back to the original character.
	 * @param index
	 * @return char
	 */
	public static char charOf(int index)
	{
		char c = '?';

		if (index < 0)
		{
		}
		else if (index <= 25)
		{
			c = (char) ('A' + index);
		}
		else if (index <= 30)
		{
			c = (char) ('<' + index - 26);
		}
		else if (index <= 32)
		{
			c = (char) (' ' + index - 31);
		}
		else if (index == 33)
		{
			c = '^';
		}
		else if (index <= 35)
		{
			c = (char) ('$' + index - 34);
		}
		else if (index <= SEMICOLON)
		{
			c = (char) (39 + index - 36);
		}
		else if (index == UNDERLINE)
		{
			c = '_';
		}
		else if (index == PARA)
		{
			c = '�';
		}
		return c;
	}

	public static int symbolOf(char c)
	{
		return symbolOf(c, QUESTION_MARK);
	}

	/**
	 * @param c
	 *            Character
	 * @return Returns the index for the given character.
	 */
	public static int symbolOf(char c, int defaultIndex)
	{
		int i = c;
		int e = i - 32;

		// upper case letters A-Z
		if ((i >= 'A') && (i <= 'Z'))
		{
			i -= 'A';
		}
		// lower case letters a-z
		else if ((i >= 'a') && (i <= 'z'))
		{
			i -= 'a';
		}
		// indices array
		else if ((e >= 0) && (e < INDICES.length))
		{
			i = INDICES[e];
		}
		// ^
		else if (i == '^')
		{
			i = ROOF;
		}
		// convert _ to -
		else if (i == '_')
		{
			i = UNDERLINE;
		}
		else if (i == '�')
		{
			i = PARA;
		}
		// else: invalid
		else
		{
			i = -1;
		}

		if (i == INVALID)
			i = defaultIndex;

		return i;
	}

	/**
	 * Checks if the given character is suitable for being followed by a line break.
	 * @param c
	 */
	public static boolean isSeparator(char c)
	{
		boolean b = (c == ' ') || (c == '!') || (c == '%') || ((c >= ')') && (c <= '.')) || ((c >= ':') && (c <= ';')) || (c == '=')
				|| (c == '?');
		return b;
	}

	public static boolean isValid(char c)
	{
		int i = symbolOf(c, -1);
		return (i >= 0);
	}

	public static boolean isLetter(char c)
	{
		return (c >= 'A' && c <= 'Z');
	}

	public static boolean isNumber(char c)
	{
		return (c >= '0' && c <= '9');
	}

	public static boolean isSpace(char c)
	{
		return (c == ' ');
	}

	public static boolean isHyphen(char c)
	{
		return (c == '-');
	}

	/*
	public static void main(String[] args)
	{
		for (int i = 0; i < 64; ++i)
			System.out.println("[" + i + "]: " + charOf(i));

		// [59..63] still available
	}
	*/
}
