package xoric.prism.data.types;

public interface IText_r
{
	@Override
	public boolean equals(Object obj);

	@Override
	public String toString();

	public byte symbolAt(int index);

	public String substring(int beginIndex, int endIndex);

	public String substring(int beginIndex);

	/**
	 * Returns the ascii character at the given position.
	 * @param index
	 * @return Character
	 */
	public char charAt(int index);

	/**
	 * Returns the length.
	 * @return int
	 */
	public int length();

	public String cut(int count);
}
