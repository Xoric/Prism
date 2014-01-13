package xoric.prism.swing.tooltips;

public abstract class ToolTipFormatter
{
	private static final int maxCharsPerLine = 40;

	public static String split(String tooltip)
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
