package xoric.prism.client.bootstrap;

import xoric.prism.client.PrismClient;
import xoric.prism.client.settings.ClientSettings;
import xoric.prism.com.MessageBase;
import xoric.prism.com.Perspective;
import xoric.prism.com.PrismCommunicationLoader;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.scene.lwjgl.textures.TextureBinderLWJGL;
import xoric.prism.scene.materials.Drawer;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.materials.Printer;
import xoric.prism.scene.textures.ITextureBinder;
import xoric.prism.world.PrismWorldLoader;

public class PrismClientBootstrap
{
	private static PrismSceneLWJGL scene;
	private static ITextureBinder textureBinder;
	private static PrismClient client;
	private static ClientSettings settings;

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

			// initialize
			PrismDataLoader.loadAll();
			PrismWorldLoader.loadAll(false);
			PrismCommunicationLoader.loadAll();

			// set message perspective
			MessageBase.perspective = Perspective.CLIENT;

			// create client settings
			settings = new ClientSettings();

			// create scene and initialize materials
			scene = new PrismSceneLWJGL();
			scene.loadSettings(settings);
			textureBinder = new TextureBinderLWJGL();
			//			AllShaders.load(scene); 
			Materials.load(textureBinder);
			Drawer.renderer = scene;
			Printer.renderer = scene;

			// create scene and client
			client = new PrismClient(scene);

			// start client
			client.testConnect();
			client.start();
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
