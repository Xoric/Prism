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
import xoric.prism.scene.art.ITextureBinder;
import xoric.prism.scene.camera.Camera;
import xoric.prism.scene.camera.CameraGL;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.scene.lwjgl.shaders.ShaderIOLWJGL;
import xoric.prism.scene.lwjgl.textures.TextureBinderLWJGL;
import xoric.prism.scene.shaders.IShaderIO;
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

	//	private static IFrameBufferIO frameBufferIO;

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
			Camera cam = new CameraGL(0.0f, 0.0f, 640.0f, 400.0f);
			scene = new PrismSceneLWJGL(cam);
			textureBinder = new TextureBinderLWJGL();
			shaderIO = new ShaderIOLWJGL();
			//			frameBufferIO = new FrameBufferIOLWJGL(scene);

			// create objects
			SceneHandler sceneHandler = new SceneHandler(scene, scene.getUIRenderer(), textureBinder, shaderIO);
			MainView view = new MainView(sceneHandler);
			MainControl control = new MainControl(view);
			view.setControl(control);

			// start client
			sceneHandler.start();
			view.setVisible(true);

			// load materials
			//			Materials.loadAll(scene.getUIRenderer(), textureBinder, shaderIO); (moved to SceneHandler thread)
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
