package xoric.prism.creator.bootstrap;

import xoric.prism.creator.drawer.control.DrawerControl2;
import xoric.prism.creator.drawer.view.DrawerView2;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions2.PrismException2;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.scene.IScene;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.world.loader.PrismWorldLoader;

public class PrismCreatorBootstrap
{
	private static IScene scene;

	private static DrawerView2 drawerView;
	private static DrawerControl2 drawerControl;

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
			PrismWorldLoader.loadAll(true);

			// create scene
			scene = new PrismSceneLWJGL();

			// setup Drawer
			drawerView = new DrawerView2(scene);
			drawerControl = new DrawerControl2(drawerView);
			drawerView.setControl(drawerControl);

			// start creator
			drawerView.start();
		}
		catch (PrismException2 e)
		{
			e.user.showMessage();
			e.code.print();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
