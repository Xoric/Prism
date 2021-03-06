package xoric.prism.server.view;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import xoric.prism.swing.PrismPanel;

public class ServerConsole extends PrismPanel
{
	private static final long serialVersionUID = 1L;

	public ServerConsole()
	{
		super("Console");

		//		JTextArea area = new JTextArea();
		JTextPane area = new JTextPane();

		JScrollPane scroll = new JScrollPane(area);
		MessageConsole mc = new MessageConsole(area);
		//		mc.redirectOut();
		//		mc.redirectErr(Color.RED, null);
		mc.redirectAll();
		mc.setMessageLines(50);

		this.setContent(scroll);
	}
}
