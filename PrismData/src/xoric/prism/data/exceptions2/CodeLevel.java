package xoric.prism.data.exceptions2;

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
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		// add error message
		if (text != null)
			sb.append(text);
		else
			sb.append("An error occured.");
		sb.append('\n');

		// add additional information
		this.extractInfoAsText(sb);

		// add stack trace
		sb.append('\n');
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		sb.append(sw.toString());

		// add original exception
		if (originalException != null)
		{
			sb.append("\nsource: " + originalException.toString() + '\n');

			StringWriter sw2 = new StringWriter();
			PrintWriter pw2 = new PrintWriter(sw2);
			originalException.printStackTrace(pw2);
			sb.append(sw2.toString());
		}

		return sb.toString();
	}
}
