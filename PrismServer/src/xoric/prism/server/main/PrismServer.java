package xoric.prism.server.main;

import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ModuleID;
import xoric.prism.swing.PrismFrame;

public class PrismServer
{
	private static PrismFrame server;

	public static void main(String args[])
	{
		// register as default module
		ErrorCode.defaultModuleID = ModuleID.SERVER;

		server = new PrismFrame("Prism Server", 800, 480, true);
		server.setVisible(true);
	}
}