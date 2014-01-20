package xoric.prism.server.bootstrap;

import javax.swing.SwingUtilities;

import xoric.prism.global.PrismGlobal;
import xoric.prism.server.control.ServerControl;
import xoric.prism.server.main.ServerMain;
import xoric.prism.server.main.ServerModel;
import xoric.prism.server.view.ServerView;

public class PrismServerBootstrap
{
	public static void main(String[] args)
	{
		//		try
		//		{
		// global initialization
		PrismGlobal.setLookAndFeel();
		//			PrismGlobal global = new PrismGlobal();
		//			global.load();
		//			Prism.global = global;

		// initialize
		//			PrismDataLoader.loadAll();
		//			PrismWorldLoader.loadAll(false);

		// create scene and client
		final ServerModel model = new ServerModel();
		final ServerControl control = new ServerControl(model);
		final ServerView view = new ServerView(model);

		control.setView(view);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				view.setControl(control);
				view.displayAll();
				view.setVisible(true);
			}
		});

		ServerMain.run();
	}
}
