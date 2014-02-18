package xoric.prism.data.types;

/**
 * @author XoricLee
 * @since 29.04.2011, 16:09:47
 */
public class Word
{
	public int start;
	public int length;

	public Word(int start, int length)
	{
		set(start, length);
	}

	public Word(Word word)
	{
		set(word.start, word.length);
	}

	public boolean checkInside(int index)
	{
		return (index >= start && index < calcEnd());
	}

	@Override
	public String toString()
	{
		return "start=" + start + ", len=" + length;
	}

	/**
	 * @return start + length
	 */
	public int calcEnd()
	{
		return start + length;
	}

	public void set(int start, int length)
	{
		this.start = start;
		this.length = length;
	}

	/**
	 * Extracts a substring from the given String.
	 * @param text
	 * @return String
	 */
	public String extractFrom(String text)
	{
		String result;

		if (start >= 0 && length > 0 && calcEnd() <= text.length())
			result = text.substring(start, calcEnd());
		else
			result = "";

		return result;
	}

	/**
	 * Copy from another Word.
	 * @param word
	 */
	public void copyFrom(Word word)
	{
		set(word.start, word.length);
	}

	/**
	 * Extracts the next word from a string.
	 * @param s
	 *            String to extract the word from
	 * @param start
	 *            Starting position
	 * @return Word or null
	 */
	public static Word nextWord(String s, int start)
	{
		int i = start;
		int sLength = s.length();
		boolean seperatorFound = false;
		boolean isSeperator = true;

		// find the next index for a potential line break
		while (i < sLength && !(seperatorFound && !isSeperator))
		{
			char c = s.charAt(i);
			isSeperator = TextMap.isSeparator(c); // potential line break (' ', '.', ...)
			seperatorFound |= isSeperator;
			++i;
		}

		// calculate the length of the found word
		if (i < sLength)
			--i;
		int length = i - start;

		// create a word
		Word word = length > 0 ? new Word(start, length) : null;

		// return result
		return word;
	}
}
