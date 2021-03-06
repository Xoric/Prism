package xoric.prism.creator.common.view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AboutDialog
{
	private final JLabel aboutLabel;
	private final String creatorName;
	private final List<Component> components;

	private StringBuffer info;

	public AboutDialog(String creatorName)
	{
		this.creatorName = creatorName;

		aboutLabel = new JLabel();
		components = new ArrayList<Component>();
		components.add(aboutLabel);

		info = new StringBuffer();
		info.append("<html>");
		info.append("Copyright � 2014 Xoric.<br>");
	}

	public void appendHtmlLine(String line)
	{
		info.append("<br>" + line);
	}

	public void showAbout()
	{
		Object[] message = new Object[components.size()];

		aboutLabel.setText(info.toString() + "</html>");

		for (int i = 0; i < components.size(); ++i)
			message[i] = components.get(i);

		String[] options = { "Close" };
		JOptionPane.showOptionDialog(null, message, "About " + creatorName, JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				options, options[0]);
	}

	public void appendComponent(Component c)
	{
		components.add(c);
	}
}
