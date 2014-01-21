package xoric.prism.client.bootstrap;

import xoric.prism.client.main.PrismClient;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.scene.IScene;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.world.loader.PrismWorldLoader;

public class PrismClientBootstrap
{
	private static IScene scene;
	private static PrismClient client;

	public static void main(String[] args)
	{
		try
		{
			// global initialization
			PrismGlobal.setLookAndFeel();
			PrismGlobal global = new PrismGlobal();
			global.load();
			Prism.global = global;

			PrismDataLoader.loadAll();
			PrismWorldLoader.loadAll(false);

			// create scene and client
			scene = new PrismSceneLWJGL();
			client = new PrismClient(scene);

			// start client
			client.start();
			client.testConnect();
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
