package xoric.prism.creator.bootstrap;

import xoric.prism.creator.drawer.control.DrawerControl;
import xoric.prism.creator.drawer.view.DrawerView;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.scene.IScene;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.world.PrismWorldLoader;

public class PrismCreatorBootstrap
{
	private static IScene scene;

	private static DrawerView drawerView;
	private static DrawerControl drawerControl;

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
			drawerView = new DrawerView(scene);
			drawerControl = new DrawerControl(drawerView);
			drawerView.setControl(drawerControl);

			// start creator
			drawerView.start();
		}
		catch (PrismException e)
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
