package xoric.prism.data.exceptions2;

import java.io.PrintStream;
import java.io.PrintWriter;

public class PrismException2 extends Throwable implements IInfoContainer
{
	private static final long serialVersionUID = 1L;

	private final Exception originalException;

	public final UserLevel user;
	public final CodeLevel code;

	public PrismException2()
	{
		this.originalException = null;
		this.user = new UserLevel();
		this.code = new CodeLevel(this);
	}

	public PrismException2(Exception originalException)
	{
		this.originalException = originalException;
		this.user = new UserLevel();
		this.code = new CodeLevel(this, originalException);
	}

	@Override
	public void printStackTrace()
	{
		if (originalException != null)
			originalException.printStackTrace();
		else
			super.printStackTrace();
	}

	@Override
	public void printStackTrace(PrintWriter w)
	{
		if (originalException != null)
			originalException.printStackTrace(w);
		else
			super.printStackTrace(w);
	}

	@Override
	public void printStackTrace(PrintStream s)
	{
		if (originalException != null)
			originalException.printStackTrace(s);
		else
			super.printStackTrace(s);
	}

	@Override
	public String toString()
	{
		return code.toString();
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
