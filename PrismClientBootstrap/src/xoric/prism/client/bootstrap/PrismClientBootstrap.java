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
import xoric.prism.scene.art.ITextureBinder;
import xoric.prism.scene.camera.Camera;
import xoric.prism.scene.camera.CameraGL;
import xoric.prism.scene.fbo.IFrameBufferIO;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;
import xoric.prism.scene.lwjgl.shaders.ShaderIOLWJGL;
import xoric.prism.scene.lwjgl.textures.TextureBinderLWJGL;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.renderer.IUIRenderer2;
import xoric.prism.scene.shaders.IShaderIO;
import xoric.prism.world.PrismWorldLoader;

public class PrismClientBootstrap
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
			PrismWorldLoader.loadAll(false);
			PrismCommunicationLoader.loadAll();

			// set message perspective
			MessageBase.perspective = Perspective.CLIENT;

			// create client settings
			settings = new ClientSettings();

			// create camera
			Camera cam = new CameraGL(0.0f, 0.0f, 640.0f, 400.0f);
			//			Camera cam = new CameraOld(0.0f, 0.0f, 640.0f, 400.0f);
			//			cam.set(settings., size);

			// create scene objects
			scene = new PrismSceneLWJGL(cam);
			scene.loadSettings(settings);
			textureBinder = new TextureBinderLWJGL();
			shaderIO = new ShaderIOLWJGL();
			//			frameBufferIO = new FrameBufferIOLWJGL(scene);

			// create renderers
			//			IWorldRenderer2 worldRenderer = scene.getWorldRenderer();
			IUIRenderer2 uiRenderer = scene.getUIRenderer();

			// create window and GL context
			client = new PrismClient(scene, cam);
			client.createWindow();

			// load materials
			Materials.loadAll(uiRenderer, textureBinder, shaderIO);

			// initialize materials
			//			AllShaders.load(scene); 

			// start client
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
