/**
 * 
 */
package xoric.prism.creator.ground;

import xoric.prism.creator.ground.control.MainControl;
import xoric.prism.creator.ground.view.MainView;
import xoric.prism.creator.ground.view.SceneHandler;
import xoric.prism.data.PrismDataLoader;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.global.PrismGlobal;
import xoric.prism.scene.fbo.FrameBufferIOLWJGL;
import xoric.prism.scene.fbo.IFrameBufferIO;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.scene.lwjgl.shaders.ShaderIOLWJGL;
import xoric.prism.scene.lwjgl.textures.TextureBinderLWJGL;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.shaders.IShaderIO;
import xoric.prism.scene.textures.ITextureBinder;
import xoric.prism.world.PrismWorldLoader;

/**
 * @author XoricLee
 * @since 22.02.2014, 13:45:09
 */
public class PrismCreatorGround
{
	private static PrismSceneLWJGL scene;
	private static ITextureBinder textureBinder;
	private static IShaderIO shaderIO;
	private static IFrameBufferIO frameBufferIO;

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

			// create scene objects
			scene = new PrismSceneLWJGL();
			textureBinder = new TextureBinderLWJGL();
			shaderIO = new ShaderIOLWJGL();
			frameBufferIO = new FrameBufferIOLWJGL();

			// load materials
			Materials.loadAll(scene, textureBinder, shaderIO);

			// create objects
			SceneHandler sceneHandler = new SceneHandler(scene);
			MainView view = new MainView(sceneHandler);
			MainControl control = new MainControl(view);
			view.setControl(control);

			// start client
			//			client.testConnect();
			view.start();
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
