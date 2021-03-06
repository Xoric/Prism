package xoric.prism.creator.common.factory;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import xoric.prism.data.tools.Common;

public class SuccessMessage
{
	private static final String p1 = "<tr><td>";
	private static final String p2 = "</td><td><code>";
	private static final String p3 = "</code></td></tr>";

	private final StringBuffer sb;
	private final String dataName;
	private ImageIcon icon;

	public SuccessMessage(String dataName)
	{
		this.dataName = dataName;

		sb = new StringBuffer();
		sb.append("<html>" + dataName.substring(0, 1).toUpperCase() + dataName.substring(1) + " created successfully.<br><br>");
		sb.append("<table border=\"1\">");
	}

	public void addFile(File file)
	{
		String filename = file.getName();
		String fileSize = Common.getFileSize(file.length());

		addInfo("File", filename);
		addInfo("Size", fileSize);
	}

	public void addInfo(String key, String value)
	{
		sb.append(p1 + key + ": " + p2 + value + p3);
	}

	public void addIcon(File file)
	{
		try
		{
			icon = IconLoader.loadIconFromFile(file, 140, 140);
		}
		catch (Exception e)
		{
		}
	}

	public void showMessage()
	{
		sb.append("</table></html>");
		JOptionPane.showMessageDialog(null, sb.toString(), "Generate " + dataName, JOptionPane.INFORMATION_MESSAGE, icon);
	}
}
