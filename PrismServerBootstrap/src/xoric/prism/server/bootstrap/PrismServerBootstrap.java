package xoric.prism.server.bootstrap;

import javax.swing.SwingUtilities;

import xoric.prism.com.MessageBase;
import xoric.prism.com.Perspective;
import xoric.prism.com.PrismCommunicationLoader;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.server.control.ServerControl;
import xoric.prism.server.model.ServerModel;
import xoric.prism.server.view.ServerView;
import xoric.prism.world.PrismWorldLoader;

public class PrismServerBootstrap
{
	public static void main(String[] args)
	{
		try
		{
			// global initialization
			PrismGlobal.setLookAndFeel();
			PrismGlobal global = new PrismGlobal();
			global.init();
			global.load();
			Prism.global = global;

			// set message perspective
			MessageBase.perspective = Perspective.SERVER;

			// initialize
			PrismDataLoader.loadAll();
			PrismWorldLoader.loadAll(false);
			PrismCommunicationLoader.loadAll();

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
					view.printWelcome();
					view.displayAll();
					view.setVisible(true);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
