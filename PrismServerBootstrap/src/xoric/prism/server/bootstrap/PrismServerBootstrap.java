package xoric.prism.server.bootstrap;

import javax.swing.SwingUtilities;

import xoric.prism.com.MessageBase;
import xoric.prism.com.Perspective;
import xoric.prism.com.PrismCommunicationLoader;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.server.control.Doorman;
import xoric.prism.server.control.ServerControl;
import xoric.prism.server.db.Database;
import xoric.prism.server.db.mongo.MongoLoader;
import xoric.prism.server.net.ServerNet;
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
			final ServerControl control = new ServerControl();
			final ServerView view = new ServerView();
			final Database db = MongoLoader.loadAll();

			// create server objects
			Doorman doorman = new Doorman();
			ServerNet network = new ServerNet();

			// register all objects
			control.register(view, doorman, network);
			view.register(control, network);
			doorman.register(db.accounts);

			// create server frame
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
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
