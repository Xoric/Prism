package xoric.prism.server.bootstrap;

import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.server.main.PrismServer;
import xoric.prism.world.loader.PrismWorldLoader;

public class PrismServerBootstrap
{
	private static PrismServer server;

	public static void main(String[] args)
	{
		try
		{
			// global initialization
			PrismGlobal.setLookAndFeel();
			PrismGlobal global = new PrismGlobal();
			global.load();
			Prism.global = global;

			// initialize
			PrismDataLoader.loadAll();
			PrismWorldLoader.loadAll(false);

			// create scene and client
			server = new PrismServer();

			// start client
			server.start();
		}
		catch (PrismException e)
		{
			e.code.print();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
