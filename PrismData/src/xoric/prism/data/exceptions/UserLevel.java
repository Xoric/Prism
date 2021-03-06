package xoric.prism.data.exceptions;

import javax.swing.JOptionPane;

public class UserLevel extends InfoContainer
{
	public void showMessage()
	{
		JOptionPane.showMessageDialog(null, toHtml(), "Error", JOptionPane.WARNING_MESSAGE);
	}

	public String toHtml()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");

		// add error message
		sb.append("<span style=\"background-color:#DCDCDC; color:#0;\"><strong>");
		if (text != null)
			sb.append(text);
		else
			sb.append("An error occured.");
		sb.append("</span><br>");

		// add additional information
		this.extractInfoAsHtml(sb);

		sb.append("</html>");
		return sb.toString();
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

		// add additional information
		this.extractInfoAsText(sb);

		return sb.toString();
	}
}
