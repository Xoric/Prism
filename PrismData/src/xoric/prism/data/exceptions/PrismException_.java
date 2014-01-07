package xoric.prism.data.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import xoric.prism.data.modules.ErrorCode_;

@Deprecated
public class PrismException_ extends Exception
{
	private static final long serialVersionUID = 1L;

	private ErrorCode_ errorCode;

	private final List<InfoPair_> infoPairs;
	private final List<String> infoLines;

	public PrismException_(ErrorCode_ errorCode)
	{
		this.errorCode = errorCode;

		this.infoPairs = new ArrayList<InfoPair_>();
		this.infoLines = new ArrayList<String>();
	}

	public ErrorCode_ getErrorCode()
	{
		return errorCode;
	}

	public final void appendOriginalException(Exception e)
	{
		appendInfo(e.toString());
	}

	public final void appendInfo(String value)
	{
		infoLines.add(value);
	}

	public final void appendInfo(String key, String value)
	{
		InfoPair_ p = new InfoPair_(key, value);
		infoPairs.add(p);
	}

	public final void appendExpectedInfo(String error, int expected, int found)
	{
		String l = error + " (" + expected + " expected, " + found + " found)";
		infoLines.add(l);
	}

	public void showUserErrorMessage()
	{
		StringBuffer sb = new StringBuffer("<html>");

		sb.append("An error occured" + "<br><br>");

		sb.append(errorCode.getErrorID().getDescription() + "<br>");
		sb.append("<b>Module:</b> " + errorCode.getModuleID() + "<br>");
		sb.append("<b>Actor:</b> " + errorCode.getActorID() + "<br>");
		sb.append("<b>Error:</b> " + errorCode.getErrorID() + "<br>");
		sb.append("<br>");

		for (InfoPair_ p : infoPairs)
			sb.append(p.key + ": " + p.value + "<br>");

		for (String l : infoLines)
			sb.append(l + "<br>");

		sb.append("<br></html>\n");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace(pw);
		sb.append(sw.toString());

		JOptionPane.showMessageDialog(null, sb.toString(), "Exception", JOptionPane.WARNING_MESSAGE);
	}

	public void showErrorMessage()
	{
		StringBuffer sb = new StringBuffer("<html>");

		sb.append(super.toString() + "<br><br>");

		sb.append(errorCode.toHexString() + "<br>");
		sb.append("<b>Module:</b> " + errorCode.getModuleID() + "<br>");
		sb.append("<b>Actor:</b> " + errorCode.getActorID() + "<br>");
		sb.append("<b>Error:</b> " + errorCode.getErrorID() + "<br>");
		sb.append("<br>");

		for (InfoPair_ p : infoPairs)
			sb.append(p.key + ": " + p.value + "<br>");

		for (String l : infoLines)
			sb.append(l + "<br>");

		sb.append("<br></html>\n");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace(pw);
		sb.append(sw.toString());

		JOptionPane.showMessageDialog(null, sb.toString(), "Exception", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public final String toString()
	{
		StringBuffer sb = new StringBuffer(super.toString() + " (" + errorCode.toHexString() + "), ");

		sb.append("module=" + errorCode.getModuleID() + ", ");
		sb.append("actor=" + errorCode.getActorID() + ", ");
		sb.append("error=" + errorCode.getErrorID());

		for (InfoPair_ p : infoPairs)
			sb.append(", " + p.key + ": " + p.value);

		for (String l : infoLines)
			sb.append(", " + l);

		return sb.toString();
	}
}
