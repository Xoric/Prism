package xoric.prism.client.bootstrap;

import javax.swing.UIManager;

import xoric.prism.creator.drawer.PrismDrawer;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ModuleID;
import xoric.prism.scene.IScene;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;

public class PrismClientBootstrap
{
	private static IScene scene;
	private static PrismDrawer drawer;

	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
	}

	public static void main(String[] args)
	{
		// register as default module
		ErrorCode.defaultModuleID = ModuleID.CREATOR;

		// set look and feel
		setLookAndFeel();

		// create scene and client
		scene = new PrismSceneLWJGL();
		drawer = new PrismDrawer(scene);

		// start creator
		drawer.start();
	}
}
