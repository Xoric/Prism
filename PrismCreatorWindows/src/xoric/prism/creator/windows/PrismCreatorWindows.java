package xoric.prism.creator.windows;

import xoric.prism.creator.windows.control.MainControl;
import xoric.prism.creator.windows.view.MainView;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.scene.IScene;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.scene.lwjgl.textures.TextureBinderLWJGL;
import xoric.prism.scene.materials.Drawer;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.materials.Printer;
import xoric.prism.scene.textures.ITextureBinder;

public abstract class PrismCreatorWindows
{
	private static IScene scene;

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

			// create scene and initialize materials
			PrismSceneLWJGL s = new PrismSceneLWJGL();
			scene = s;
			ITextureBinder textureBinder = new TextureBinderLWJGL();
			Materials.load(textureBinder);
			Drawer.renderer = s;
			Printer.renderer = s;

			// create scene and client
			MainView view = new MainView(scene);
			MainControl control = new MainControl(view);
			view.setControl(control);
			view.setVisible(true);
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
