package xoric.prism.data.exceptions;

interface IInfoContainer
{
	public void setText(UserErrorText t);

	public void setText(String text);

	public void setText(String text, int expected, int found);

	public void setText(String text, String expected, String found);

	public void addInfo(String key, String value);

	public void addInfo(String key, int value);
}
