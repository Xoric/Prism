package xoric.prism.scene.lwjgl;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import xoric.prism.data.exceptions.IExceptionSink;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.scene.IInputListener;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.camera.ICameraTransform;
import xoric.prism.scene.lwjgl.cleanup.TrashCan;
import xoric.prism.scene.lwjgl.renderers.UIRendererLWJGL;
import xoric.prism.scene.lwjgl.renderers.WorldRendererLWJGL;
import xoric.prism.scene.renderer.IUIRenderer2;
import xoric.prism.scene.renderer.IWorldRenderer2;
import xoric.prism.scene.settings.ISceneSettings;

public class PrismSceneLWJGL implements IScene, IExceptionSink//, IFrameBufferParent
{
	private final InputHandlerLWJGL inputHandler;
	private final MouseProjectorLWJGL mouseProjector;
	private final FloatPoint mouseInWorld;

	private final ICameraTransform camera;

	private final WorldRendererLWJGL worldRenderer;
	private final UIRendererLWJGL uiRenderer;

	private FloatPoint frameSize;
	private Exception exception;

	//	private int frameBufferID;

	private float slope;

	private int fps;

	public PrismSceneLWJGL(ICameraTransform cam)
	{
		setFps(60);

		//		slope = 0.0f;
		slope = 0.15f;

		camera = cam;

		worldRenderer = new WorldRendererLWJGL(slope, cam);
		uiRenderer = new UIRendererLWJGL();

		inputHandler = new InputHandlerLWJGL();
		mouseProjector = new MouseProjectorLWJGL();

		mouseInWorld = new FloatPoint();
	}

	@Override
	public void loadSettings(ISceneSettings settings)
	{
		setFps(settings.getFps());
	}

	private void setFps(int fps)
	{
		if (fps < 30)
			fps = 30;
		else if (fps > 90)
			fps = 90;

		this.fps = fps;
	}

	private static DisplayMode findDisplay(int width, int height) throws LWJGLException
	{
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		DisplayMode result = null;
		int bestDelta = 0;

		for (int i = 0; i < modes.length; i++)
		{
			int delta = Math.abs(modes[i].getWidth() - width) + Math.abs(modes[i].getHeight() - height);
			if (delta < bestDelta || i == 0)
			{
				bestDelta = delta;
				result = modes[i];
			}
		}
		return result;
	}

	public IWorldRenderer2 getWorldRenderer()
	{
		return worldRenderer;
	}

	public IUIRenderer2 getUIRenderer()
	{
		return uiRenderer;
	}

	/* **************** IScene *************************** */

	@Override
	public IPoint_r findBestResolution(int width, int height)
	{
		Point p;
		try
		{
			DisplayMode m = findDisplay(width, height);
			p = new Point(m.getWidth(), m.getHeight());
		}
		catch (LWJGLException e)
		{
			p = null;
		}
		return p;
	}

	@Override
	public List<Dimension> getAvailableResolutions()
	{
		List<Dimension> result = new ArrayList<Dimension>();
		try
		{
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++)
				result.add(new Dimension(modes[i].getWidth(), modes[i].getHeight()));
		}
		catch (LWJGLException e)
		{
			result.clear();
		}
		return result;
	}

	@Override
	public IFloatPoint_r createWindow(int width, int height, boolean isFullScreen) throws PrismException
	{
		try
		{
			DisplayMode mode = findDisplay(width, height);
			System.out.println("Setting display size to " + mode.getWidth() + " x " + height);
			frameSize = new FloatPoint(mode.getWidth(), mode.getHeight());
			uiRenderer.setScreenSize(frameSize);
			Display.setDisplayMode(mode);
			Display.create();
		}
		catch (LWJGLException e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("An error occured while trying to create a display window.");
			throw e;
		}
		return frameSize;
	}

	@Override
	public void setFullScreen(boolean fullscreen)
	{
		try
		{
			Display.setFullscreen(fullscreen);
		}
		catch (LWJGLException e)
		{
		}
	}

	@Override
	public void initialize() throws PrismException
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);

		//		GL11.glEnable(GL11.GL_DEPTH_TEST); // TODO: just a test
	}

	@Override
	public IFloatPoint_r getMouseOnScreen()
	{
		return inputHandler.getMouseOnScreen();
	}

	@Override
	public IFloatPoint_r getMouseInWorld()
	{
		return mouseInWorld;
	}

	@Override
	public void startLoop(ISceneListener client, IInputListener il)
	{
		inputHandler.initialize(il, frameSize);

		long lastMs = System.currentTimeMillis();
		boolean resumeTimer = true;

		/* test */GL11.glMatrixMode(GL11.GL_PROJECTION); // Den richtigen Stack aktivieren
		GL11.glLoadIdentity(); // Die Matrix zuruecksetzen

		int frameCounter = 0;
		int frameTimerMs = 0;

		//		setInterpolationNearest();

		do
		{
			long currentMs = System.currentTimeMillis();
			int passedMs = (int) (currentMs - lastMs);

			// handle controls
			inputHandler.update(passedMs);

			if (passedMs > 0)
			{
				frameTimerMs += passedMs;
				if (frameTimerMs >= 3000)
				{
					int fps = (frameCounter * 1000) / frameTimerMs;
					Display.setTitle(fps + " fps");
					frameCounter = 0;
					frameTimerMs = 0;
				}

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

				// client returns false if the scene should be closed
				if (resumeTimer)
				{
					setStage(true);
					resumeTimer = client.update(passedMs);
				}
				if (resumeTimer)
				{
					try
					{
						setStage(true);
						client.drawWorld(worldRenderer);

						// calculate mouseInWorld
						mouseProjector.update();
						mouseInWorld.x = 0.5f + mouseProjector.getMouseInWorld().getX();
						mouseInWorld.y = 0.5f - mouseProjector.getMouseInWorld().getY();
						camera.transformNormalizedScreenToWorld(mouseInWorld, mouseInWorld);

						setStage(false);
						client.drawUI(uiRenderer);

						// TEST FRAME BUFFER
						/*
						EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, frameBufferID); // set frame buffer as render target
						int anyTextureID = Materials.env0.getTexture(0).getTextureID();
						EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
								EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, anyTextureID, 0);
						EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0); // release frame buffer (target is window again)
						int res = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
						if (res != EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT)
						{
							PrismException e = new PrismException();
							e.setText("frame buffer incomplete");
							receiveException(e);
						}
						*/
					}
					catch (Exception e)
					{
						receiveException(e);
					}
				}

				// update the scene
				Display.update();
				++frameCounter;
				resumeTimer &= !Display.isCloseRequested();

				// keep track of time passed
				lastMs = currentMs;
			}

			Display.sync(fps); // cap fps

			resumeTimer &= exception == null;
		}
		while (resumeTimer);

		// destroy scene
		TrashCan.cleanUp();
		Display.destroy();
		client.onClosingScene(exception);
	}

	public void setStage(boolean isWorldStage)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		if (isWorldStage)
		{
			GL11.glFrustum(-0.5, 0.5, -0.5, 0.5, 1.5, 6.5);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
		else
		{
			GL11.glOrtho(0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glLoadIdentity();

		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
	}

	/* **************** IRenderer *************************** */

	public void setInterpolationNearest()
	{
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}

	@Override
	public void setSlope(float slope)
	{
		this.slope = slope;
		this.worldRenderer.setSlope(slope);
		this.inputHandler.onSlopeChanged();
	}

	@Override
	public IFloatPoint_r getScreenSize()
	{
		return frameSize;
	}

	@Override
	public void setWindowTitle(String title)
	{
		Display.setTitle(title);
	}

	// IScene:
	@Override
	public float getSlope()
	{
		return slope;
	}

	@Override
	public void receiveException(Exception e)
	{
		if (exception == null)
			exception = e;
	}
	/*
		@Override
		public void testMouseTransform(float x, float y)
		{
			//		test2(x, y);
		}

		private static final FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		private static final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		private static final IntBuffer viewport = BufferUtils.createIntBuffer(16);
		private static final FloatBuffer screenDepth = BufferUtils.createFloatBuffer(1);
		private static final FloatBuffer posExact = BufferUtils.createFloatBuffer(3);

		private void test2()
		{
			int x = Mouse.getX();
			int y = Mouse.getY();

			// get the current modelView matrix, projection matrix and viewport
			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
			GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
			GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

			// read the screen depth under the mouse pointer
			GL11.glReadPixels(x, y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, screenDepth);

			// transform mouse position to world coordinates
			boolean b = GLU.gluUnProject(x, y, screenDepth.get(0), modelview, projection, viewport, posExact);

			String s;
			if (b)
			{
				s = "mouse: " + x + ", " + y + ", " + screenDepth.get(0) + " -> world: " + decimalFormat.format(posExact.get(0)) + ", "
						+ decimalFormat.format(posExact.get(1)) + ", " + decimalFormat.format(posExact.get(2));
			}
			else
			{
				s = "gluUnProject returned false";
			}
			Display.setTitle(s);
		}

		private static void printMatrix(String name, FloatBuffer f)
		{
			int i = 0;
			System.out.println(name + "Matrix:");

			for (int y = 0; y < 4; ++y)
			{
				for (int x = 0; x < 4; ++x)
				{
					System.out.print(f.get(i++));
					if (x < 4 - 1)
						System.out.print(",");
					System.out.print("\t");
				}
				System.out.println();
			}
			System.out.println(".");
		}

		private static DecimalFormat decimalFormat = new DecimalFormat("#.####");
		static
		{
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');
			decimalFormat.setMinimumFractionDigits(4);
			decimalFormat.setDecimalFormatSymbols(otherSymbols);
		}
		*/
}
