package xoric.prism.creator.windows;

import xoric.prism.client.PrismClient;
import xoric.prism.client.settings.ClientSettings;
import xoric.prism.creator.windows.control.MainControl;
import xoric.prism.creator.windows.view.MainView;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.scene.art.ITextureBinder;
import xoric.prism.scene.fbo.IFrameBufferIO;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.scene.lwjgl.fbo.FrameBufferIOLWJGL;
import xoric.prism.scene.lwjgl.shaders.ShaderIOLWJGL;
import xoric.prism.scene.lwjgl.textures.TextureBinderLWJGL;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.shaders.IShaderIO;

public abstract class PrismCreatorWindows
{
	private static PrismSceneLWJGL scene;
	private static ITextureBinder textureBinder;
	private static IShaderIO shaderIO;
	private static IFrameBufferIO frameBufferIO;
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

			// create scene objects
			scene = new PrismSceneLWJGL();
			scene.loadSettings(settings);
			textureBinder = new TextureBinderLWJGL();
			shaderIO = new ShaderIOLWJGL();
			frameBufferIO = new FrameBufferIOLWJGL(scene);

			// load materials
			Materials.loadAll(scene, textureBinder, shaderIO, frameBufferIO);

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
