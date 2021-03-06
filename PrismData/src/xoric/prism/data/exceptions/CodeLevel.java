package xoric.prism.data.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CodeLevel extends InfoContainer
{
	private final Throwable throwable;
	private final Exception originalException;

	public CodeLevel(Throwable t)
	{
		this.throwable = t;
		this.originalException = null;
	}

	public CodeLevel(Throwable t, Exception originalException)
	{
		this.throwable = t;
		this.originalException = originalException;
	}

	public void print()
	{
		System.out.println(toString());

		if (originalException != null)
			originalException.printStackTrace();
		else
			throwable.printStackTrace();
	}

	public void extractStackTrace(StringWriter sw)
	{
		PrintWriter pw = new PrintWriter(sw);

		if (originalException != null)
			originalException.printStackTrace(pw);
		else
			throwable.printStackTrace(pw);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		// add error message
		if (text != null)
			sb.append(text);
		else
			sb.append("An unknown error occured.");
		sb.append('\n');

		// add original exception
		if (originalException != null)
			sb.append("origin: " + originalException.toString() + '\n');

		// add additional information
		this.extractInfoAsText(sb);

		// return text
		return sb.toString();
	}
}
