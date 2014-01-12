package xoric.prism.data.exceptions;

interface IInfoContainer
{
	public void setText(UserErrorText t);

	public void setText(String text);

	public void addInfo(String key, String value);

	public void addInfo(String key, int value);
}