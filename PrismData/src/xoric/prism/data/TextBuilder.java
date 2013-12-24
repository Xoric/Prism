package xoric.prism.data;

class TextBuilder extends Text
{
	public void setLength(int length)
	{
		symbols.setLength(length);
		ascii.setLength(length);
	}

	public void setSymbols(int index, byte[] symbols, int count)
	{
		for (int n = 0; n < count; ++n)
		{
			char c = (char) symbols[n];
			char a = TextMap.charOf(c);
			ascii.setCharAt(index + n, a);
			symbols.setCharAt(index + n, c);
		}
	}
}
