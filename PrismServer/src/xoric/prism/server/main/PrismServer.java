package xoric.prism.server.main;

import xoric.prism.swing.PrismFrame;

public class PrismServer
{
	private static PrismFrame server;

	public void start()
	{
		server = new PrismFrame("Prism Server", 800, 480, true);
		server.setVisible(true);
	}
}
