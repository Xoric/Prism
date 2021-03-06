package xoric.prism.data.exceptions;

import java.util.ArrayList;
import java.util.List;

class InfoContainer implements IInfoContainer
{
	private final List<InfoPair> infoPairs = new ArrayList<InfoPair>();
	protected String text;

	@Override
	public void setText(UserErrorText t)
	{
		this.text = t.toString();
	}

	@Override
	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public void setText(String text, int expected, int found)
	{
		this.text = text + " (" + expected + " expected, " + found + " found)";
	}

	@Override
	public void setText(String text, String expected, String found)
	{
		this.text = text + " (" + expected + " expected, " + found + " found)";
	}

	@Override
	public void addInfo(String key, String value)
	{
		this.infoPairs.add(new InfoPair(key, value));
	}

	@Override
	public void addInfo(String key, int value)
	{
		this.infoPairs.add(new InfoPair(key, String.valueOf(value)));
	}

	public void extractInfoAsText(StringBuilder sb)
	{
		for (int i = 0; i < infoPairs.size(); ++i)
		{
			InfoPair p = infoPairs.get(i);
			if (p != null)
			{
				if (i > 0)
					sb.append('\n');
				sb.append(p.key + ": " + p.value);
			}
		}
	}

	public void extractInfoAsHtml(StringBuilder sb)
	{
		for (int i = 0; i < infoPairs.size(); ++i)
		{
			InfoPair p = infoPairs.get(i);
			if (p != null)
			{
				if (i > 0)
					sb.append("<br>");
				sb.append(p.key + ": <code>" + p.value + "</code>");
			}
		}
	}

	public String getText()
	{
		return text;
	}
}
