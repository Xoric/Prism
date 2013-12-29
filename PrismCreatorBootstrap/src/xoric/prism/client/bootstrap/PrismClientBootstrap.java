package xoric.prism.client.bootstrap;

import xoric.prism.creator.PrismCreator;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ModuleID;
import xoric.prism.scene.IScene;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;

public class PrismClientBootstrap
{
	private static IScene scene;
	private static PrismCreator creator;

	public static void main(String[] args)
	{
		// register as default module
		ErrorCode.defaultModuleID = ModuleID.CREATOR;

		// create scene and client
		scene = new PrismSceneLWJGL();
		creator = new PrismCreator(scene);

		// start creator
		creator.start();
	}
}
