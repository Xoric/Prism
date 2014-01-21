package xoric.prism.server.view;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import xoric.prism.swing.PrismPanel;

public class ServerConsole extends PrismPanel
{
	private static final long serialVersionUID = 1L;

	public ServerConsole()
	{
		super("Console");

		JTextArea area = new JTextArea();

		JScrollPane scroll = new JScrollPane(area);
		MessageConsole mc = new MessageConsole(area);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(50);

		this.setContent(scroll);
	}
}
