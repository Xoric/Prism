package xoric.prism.data.exceptions;

public class PrismException extends Throwable implements IInfoContainer
{
	private static final long serialVersionUID = 1L;

	public final UserLevel user;
	public final CodeLevel code;

	public PrismException()
	{
		this.user = new UserLevel();
		this.code = new CodeLevel(this);
	}

	public PrismException(Exception originalException)
	{
		this.user = new UserLevel();
		this.code = new CodeLevel(this, originalException);
	}

	@Override
	public void setText(UserErrorText t)
	{
		user.setText(t);
		code.setText(t);
	}

	@Override
	public void setText(String text)
	{
		user.setText(text);
		code.setText(text);
	}

	@Override
	public void setText(String text, int expected, int found)
	{
		user.setText(text, expected, found);
		code.setText(text, expected, found);
	}

	@Override
	public void addInfo(String key, String value)
	{
		user.addInfo(key, value);
		code.addInfo(key, value);
	}

	@Override
	public void addInfo(String key, int value)
	{
		user.addInfo(key, value);
		code.addInfo(key, value);
	}
}
