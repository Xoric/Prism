package xoric.prism.data.exceptions2;

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
		for (InfoPair p : infoPairs)
			sb.append('\n' + p.key + ": " + p.value);
	}

	public void extractInfoAsHtml(StringBuilder sb)
	{
		for (InfoPair p : infoPairs)
			sb.append("<br>" + p.key + ": <code>" + p.value + "</code>");
	}
}
