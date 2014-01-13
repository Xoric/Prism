package xoric.prism.swing.tooltips;

public abstract class TooltipCreator
{
	private static final int maxCharsPerLine = 30;

	public static String createTooltip(String tooltip)
	{
		StringBuffer sb = new StringBuffer("<html>");
		String[] words = tooltip.split(" ");
		boolean isFirstInLine = true;
		int sum = 0;

		for (String s : words)
		{
			if (!isFirstInLine)
				sb.append(" ");
			sb.append(s);

			isFirstInLine = (sb.length() - sum) >= maxCharsPerLine;
			if (isFirstInLine)
			{
				sum = sb.length();
				sb.append("<br>");
			}
		}
		sb.append("</html>");

		return sb.toString();
	}
}
