package xoric.prism.scene.lwjgl;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import xoric.prism.data.exceptions.IExceptionSink;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.IInputListener;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.cleanup.TrashCan;
import xoric.prism.scene.fbo.IFrameBufferParent;
import xoric.prism.scene.lwjgl.textures.Texture;
import xoric.prism.scene.settings.ISceneSettings;
import xoric.prism.scene.shaders.IDefaultShader;
import xoric.prism.scene.textures.TextureInfo;

public class PrismSceneLWJGL implements IScene, IRendererWorld, IRendererUI, IExceptionSink, IFrameBufferParent
{
	//	private final TrashCan trashCan;
	private final InputHandlerLWJGL inputHandler;
	//	private final ShaderIO2 shaderIO;
	//	private final FrameBufferIO frameBufferIO;

	private FloatPoint frameSize;
	private Exception exception;

	private int frameBufferID;

	private float slope;

	private IDefaultShader testingDefaultShader;
	private Texture testingTexture;
	private PrismColor testingColor2;
	private final boolean[] brgba = new boolean[4];

	private int interval;

	private final FloatPoint temp;
	private final FloatPoint temp2;

	public PrismSceneLWJGL()
	{
		setFps(60);

		inputHandler = new InputHandlerLWJGL();

		//		trashCan = new TrashCan();
		//		shaderIO = new ShaderIO2();
		//		frameBufferIO = new FrameBufferIO();

		temp = new FloatPoint();
		temp2 = new FloatPoint();

		slope = 0.15f;
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

		interval = 1000 / fps;
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

		//		GL11.glEnable(GL11.GL_DEPTH_TEST); // Tiefentest (mit dem Z-Buffer) aktivieren

		// generate a frame buffer
		//		boolean isFboEnabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
		//		frameBufferID = 0;
		//		if (isFboEnabled)
		//		{
		//			IntBuffer buffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); // allocate a 1 int byte buffer
		//			EXTFramebufferObject.glGenFramebuffersEXT(buffer); // generate
		//			frameBufferID = buffer.get();
		//			System.out.println("frame buffer id: " + frameBufferID);
		//		}
		//		if (frameBufferID <= 0)
		//		{
		//			PrismException e = new PrismException();
		//			e.setText(UserErrorText.FRAME_BUFFER_ERROR);
		//			throw e;
		//		}

		//		try
		//		{
		//			AllShaders.load(shaderIO);
		//		}
		//		catch (Exception e2)
		//		{
		//			receiveException(e2);
		//		}

		//		try
		//		{
		//			// TODO: throws an exception on netbook
		//			testingDefaultShader = ShaderIO2.createShader(new File("../debug/defaultShader.vert"), new File("../debug/defaultShader.frag"));
		//			testingDefaultShader.activate();
		//		}
		//		catch (PrismException e)
		//		{
		//			e.code.print();
		//		}

		/*
		testingColor2 = new PrismColor();
		testingColor2.set(1.0f, 0.0f, 0.0f, 1.0f);

		try
		{
			textures = new TextureIO[3];
			textures[0] = new TextureIO(new FileInputStream("../debug/g1.png"));
			textures[1] = new TextureIO(new FileInputStream("../debug/g2.png"));
			textures[2] = new TextureIO(new FileInputStream("../debug/john.png"));
			testingTexture = TextureBinderLWJGL.createFromFile(new File("../debug/john.png"));
			GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
			textures[0].init();
			textures[1].init();
			textures[2].init();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		*/
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

			if (interval <= 0 || passedMs >= interval)
			{
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
							client.drawWorld(this);

							setStage(false);
							client.drawUI(this);

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
			}
			else
			{
				try
				{
					Thread.sleep(interval - passedMs);
				}
				catch (InterruptedException e)
				{
					receiveException(e);
				}
			}

			resumeTimer &= exception == null;
		}
		while (resumeTimer);

		// destroy scene
		TrashCan.cleanUp();
		Display.destroy();
		client.onClosingScene(exception);
	}

	@Override
	public void setStage(boolean isWorldStage)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		if (isWorldStage)
			GL11.glFrustum(-0.5, 0.5, -0.5, 0.5, 1.5, 6.5);
		else
			GL11.glOrtho(0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f);

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
	}

	@Override
	public void drawSprite(TextureInfo texInfo, IFloatRect_r rect)
	{
		GL11.glBegin(GL11.GL_QUADS);

		float x = rect.getTopLeft().getX() / frameSize.x;
		float y = 1.0f - rect.getTopLeft().getY() / frameSize.y;
		float w = rect.getSize().getX() / frameSize.x;
		float h = -(rect.getSize().getY() / frameSize.y);

		IFloatPoint_r tl = texInfo.getRect().getTopLeft();
		IFloatPoint_r br = texInfo.getRect().getBottomRight();

		if (texInfo.isFlippedH())
		{
			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
			GL11.glVertex2f(x + w, y);
			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
			GL11.glVertex2f(x + w, y + h);
			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
			GL11.glVertex2f(x, y + h);
		}
		else
		{
			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
			GL11.glVertex2f(x + w, y);
			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
			GL11.glVertex2f(x + w, y + h);
			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
			GL11.glVertex2f(x, y + h);
		}
		GL11.glEnd();
	}

	//	@Override
	//	public void drawSprite(IFloatRect_r texRect, IFloatPoint_r position, IFloatPoint_r size)
	//	{
	//		GL11.glBegin(GL11.GL_QUADS);
	//
	//		float x = rect.getTopLeft().getX();
	//		float y = rect.getTopLeft().getY();
	//		float w = rect.getSize().getX();
	//		float h = rect.getSize().getY();
	//
	//		IFloatPoint_r tl = texRect.getTopLeft();
	//		IFloatPoint_r br = texRect.getBottomRight();
	//
	//		GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
	//		GL11.glVertex2f(x, y);
	//		GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
	//		GL11.glVertex2f(x + w, y);
	//		GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
	//		GL11.glVertex2f(x + w, y + h);
	//		GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
	//		GL11.glVertex2f(x, y + h);
	//
	//		GL11.glEnd();
	//	}

	@Override
	public void drawSprite(IFloatRect_r texRect, IFloatRect_r screenRect)
	{
		GL11.glBegin(GL11.GL_QUADS);

		/* checked */
		float x = screenRect.getTopLeft().getX() / frameSize.x;
		float y = 1.0f - screenRect.getTopLeft().getY() / frameSize.y;
		float w = screenRect.getSize().getX() / frameSize.x;
		float h = -screenRect.getSize().getY() / frameSize.y;

		IFloatPoint_r tl = texRect.getTopLeft();
		IFloatPoint_r br = texRect.getBottomRight();

		GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
		GL11.glVertex2f(x + w, y);
		GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
		GL11.glVertex2f(x + w, y + h);
		GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
		GL11.glVertex2f(x, y + h);

		GL11.glEnd();
	}

	//	@Override
	//	public void drawPlane(TextureInfo texInfo, IFloatRect_r rect)
	//	{
	//		GL11.glBegin(GL11.GL_QUADS);
	//
	//		float x = -0.5f + rect.getTopLeft().getX();
	//		float y0 = rect.getTopLeft().getY();
	//		float y = -0.5f + y0;
	//		float w = rect.getSize().getX();
	//		float h = rect.getSize().getY();
	//		float zFront = calcZ(y0);
	//		float zBack = calcZ(y0 + h);
	//
	//		IFloatPoint_r tl = texInfo.getRect().getTopLeft();
	//		IFloatPoint_r br = texInfo.getRect().getBottomRight();
	//
	//		if (texInfo.isFlippedH())
	//		{
	//			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
	//			GL11.glVertex3f(x, y, zFront);
	//			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
	//			GL11.glVertex3f(x + w, y, zFront);
	//			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
	//			GL11.glVertex3f(x + w, y + h, zBack);
	//			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
	//			GL11.glVertex3f(x, y + h, zBack);
	//		}
	//		else
	//		{
	//			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
	//			GL11.glVertex3f(x, y, zFront);
	//			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
	//			GL11.glVertex3f(x + w, y, zFront);
	//			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
	//			GL11.glVertex3f(x + w, y + h, zBack);
	//			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
	//			GL11.glVertex3f(x, y + h, zBack);
	//		}
	//
	//		//		GL11.glTexCoord2f(0.0f, 0.0f);
	//		//		GL11.glVertex3f(x, y, zFront);
	//		//		GL11.glTexCoord2f(1.0f, 0.0f);
	//		//		GL11.glVertex3f(x + w, y, zFront);
	//		//		GL11.glTexCoord2f(1.0f, 1.0f);
	//		//		GL11.glVertex3f(x + w, y + h, zBack);
	//		//		GL11.glTexCoord2f(0.0f, 1.0f);
	//		//		GL11.glVertex3f(x, y + h, zBack);
	//
	//		GL11.glEnd();
	//	}
	@Override
	public void drawPlane(TextureInfo texInfo, IFloatRect_r screenRect)
	{
		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + screenRect.getTopLeft().getX();
		float y0 = 1.0f - screenRect.getTopLeft().getY();
		float y = -0.5f + y0;
		float w = screenRect.getSize().getX();
		float h = -screenRect.getSize().getY();
		float zFront = calcZ(y0);
		float zBack = calcZ(y0 + h);

		IFloatPoint_r tl = texInfo.getRect().getTopLeft();
		IFloatPoint_r br = texInfo.getRect().getBottomRight();

		if (texInfo.isFlippedH())
		{
			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x, y, zFront);
			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x + w, y, zFront);
			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x + w, y + h, zBack);
			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x, y + h, zBack);
		}
		else
		{
			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x, y, zFront);
			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x + w, y, zFront);
			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x + w, y + h, zBack);
			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x, y + h, zBack);
		}

		GL11.glEnd();
	}

	@Override
	public void drawPlane(IFloatRect_r texRect, IFloatRect_r screenRect)
	{
		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + screenRect.getTopLeft().getX(); // TODO code duplication (see implementation above)
		float y0 = 1.0f - screenRect.getTopLeft().getY();
		float y = -0.5f + y0;
		float w = screenRect.getSize().getX();
		float h = -screenRect.getSize().getY();
		float zFront = calcZ(y0);
		float zBack = calcZ(y0 + h);

		IFloatPoint_r tl = texRect.getTopLeft();
		IFloatPoint_r br = texRect.getBottomRight();

		GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
		GL11.glVertex3f(x, y, zFront);
		GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
		GL11.glVertex3f(x + w, y, zFront);
		GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
		GL11.glVertex3f(x + w, y + h, zBack);
		GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
		GL11.glVertex3f(x, y + h, zBack);

		GL11.glEnd();
	}

	@Override
	public void drawMaskPlane(IFloatRect_r texRect, IFloatRect_r maskRect, IFloatRect_r screenRect)
	{
		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + screenRect.getTopLeft().getX(); // TODO code duplication (see implementations above)
		float y0 = 1.0f - screenRect.getTopLeft().getY();
		float y = -0.5f + y0;
		float w = screenRect.getSize().getX();
		float h = -screenRect.getSize().getY();
		float zFront = calcZ(y0);
		float zBack = calcZ(y0 + h);

		IFloatPoint_r tl0 = texRect.getTopLeft();
		IFloatPoint_r br0 = texRect.getBottomRight();

		IFloatPoint_r tl1 = maskRect.getTopLeft();
		IFloatPoint_r br1 = maskRect.getBottomRight();

		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0, tl0.getX(), 1.0f - tl0.getY());
		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE1, tl1.getX(), 1.0f - tl1.getY());
		GL11.glVertex3f(x, y, zFront);

		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0, br0.getX(), 1.0f - tl0.getY());
		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE1, br1.getX(), 1.0f - tl1.getY());
		GL11.glVertex3f(x + w, y, zFront);

		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0, br0.getX(), 1.0f - br0.getY());
		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE1, br1.getX(), 1.0f - br1.getY());
		GL11.glVertex3f(x + w, y + h, zBack);

		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0, tl0.getX(), 1.0f - br0.getY());
		GL13.glMultiTexCoord2f(GL13.GL_TEXTURE1, tl1.getX(), 1.0f - br1.getY());
		GL11.glVertex3f(x, y + h, zBack);

		GL11.glEnd();
	}

	@Override
	public void drawObject(TextureInfo texInfo, IFloatPoint_r position, IFloatPoint_r size, float z)
	{
		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + position.getX();
		float y0 = position.getY();
		// variation 1:
		//		float y = -0.5f + y0; // TODO y is currently flipped
		// variation 2:
		float y = 0.5f - y0;
		// --
		float w = size.getX();
		float h = size.getY();
		// variation 1:
		//		z = calcZ(y0) + z;
		// variation 2:
		z = calcZAlt(y0) + z;
		// --
		x -= w * 0.5f;

		//		System.out.println("renderer received texture coordinates " + texInfo.getRect().getTopLeft());

		IFloatPoint_r tl = texInfo.getRect().getTopLeft();
		IFloatPoint_r br = texInfo.getRect().getBottomRight();

		// flipping the texture's y-coordinates
		// TODO: vertically flip textures before binding?
		temp.y = 1.0f - tl.getY();
		temp2.y = 1.0f - br.getY();

		if (texInfo.isFlippedH())
		{
			GL11.glTexCoord2f(br.getX(), temp2.getY());
			GL11.glVertex3f(x, y, z);
			GL11.glTexCoord2f(tl.getX(), temp2.getY());
			GL11.glVertex3f(x + w, y, z);
			GL11.glTexCoord2f(tl.getX(), temp.getY());
			GL11.glVertex3f(x + w, y + h, z);
			GL11.glTexCoord2f(br.getX(), temp.getY());
			GL11.glVertex3f(x, y + h, z);
		}
		else
		{
			GL11.glTexCoord2f(tl.getX(), temp2.getY());
			GL11.glVertex3f(x, y, z);
			GL11.glTexCoord2f(br.getX(), temp2.getY());
			GL11.glVertex3f(x + w, y, z);
			GL11.glTexCoord2f(br.getX(), temp.getY());
			GL11.glVertex3f(x + w, y + h, z);
			GL11.glTexCoord2f(tl.getX(), temp.getY());
			GL11.glVertex3f(x, y + h, z);
		}

		//		if (texInfo.isFlippedH())
		//		{
		//			GL11.glTexCoord2f(br.getX(), tl.getY());
		//			GL11.glVertex3f(x, y, z);
		//			GL11.glTexCoord2f(tl.getX(), tl.getY());
		//			GL11.glVertex3f(x + w, y, z);
		//			GL11.glTexCoord2f(tl.getX(), br.getY());
		//			GL11.glVertex3f(x + w, y + h, z);
		//			GL11.glTexCoord2f(br.getX(), br.getY());
		//			GL11.glVertex3f(x, y + h, z);
		//		}
		//		else
		//		{
		//			GL11.glTexCoord2f(tl.getX(), tl.getY());
		//			GL11.glVertex3f(x, y, z);
		//			GL11.glTexCoord2f(br.getX(), tl.getY());
		//			GL11.glVertex3f(x + w, y, z);
		//			GL11.glTexCoord2f(br.getX(), br.getY());
		//			GL11.glVertex3f(x + w, y + h, z);
		//			GL11.glTexCoord2f(tl.getX(), br.getY());
		//			GL11.glVertex3f(x, y + h, z);
		//		}

		GL11.glEnd();
	}

	// variation 2:
	private float calcZAlt(float y)
	{
		float z = -1.5f - (1.0f - y) * slope;
		if (z > -1.499f)
			z = -1.499f;

		return z;
	}

	// variation 1:
	private float calcZ(float y)
	{
		float z = -1.5f - y * slope;
		if (z > -1.499f)
			z = -1.499f;

		return z;
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

	// IScene:
	@Override
	public void applyPerspective(FloatPoint p)
	{
		p.y = (p.y - 0.048333f) * 1.050787723f;

		float hollow = p.y < 0.5f ? p.y : 1.0f - p.y;
		hollow *= 2.0f * 0.025f;

		p.y += hollow;

		float f = 1.0f - p.y / 1.0f;
		float fx = 0.045f * f;
		float s = 1.0f / (1.0f - fx * 2.0f);

		p.x = (p.x - fx) * s;
	}

	// IExceptionSink:
	@Override
	public void receiveException(Exception e)
	{
		if (exception == null)
			exception = e;
	}

	/*
	public void draw(DrawInfo drawInfo, IFloatRect_r screenRect)
	{
		//		public void drawPlane(TextureInfo texInfo, IFloatRect_r rect);
		//		public void drawPlane(IFloatRect_r texRect, IFloatRect_r rect);

		GL11.glte
		
		FloatRectR quadRect = drawInfo.getQuadRect();

		gl2.glBegin(GL2.GL_QUADS);
		{
			// gl2.glTexCoord2f(textureRect.getX(), textureRect.getY());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE0, drawInfo.getTextureRect().getX(), drawInfo.getTextureRect().getY());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE1, textureRect2.getX(), textureRect2.getY());
			gl2.glVertex3f(quadRect.getX(), quadRect.getY(), 0.0f);

			// gl2.glTexCoord2f(textureRect.calcX2(), textureRect.getY());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE0, drawInfo.getTextureRect().calcX2(), drawInfo.getTextureRect().getY());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE1, textureRect2.calcX2(), textureRect2.getY());
			gl2.glVertex3f(quadRect.calcX2(), quadRect.getY(), 0.0f);

			// gl2.glTexCoord2f(textureRect.calcX2(), textureRect.calcY2());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE0, drawInfo.getTextureRect().calcX2(), drawInfo.getTextureRect().calcY2());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE1, textureRect2.calcX2(), textureRect2.calcY2());
			gl2.glVertex3f(quadRect.calcX2(), quadRect.calcY2(), 0.0f);

			// gl2.glTexCoord2f(textureRect.getX(), textureRect.getY2());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE0, drawInfo.getTextureRect().getX(), drawInfo.getTextureRect().calcY2());
			gl2.glMultiTexCoord2f(GL2.GL_TEXTURE1, textureRect2.getX(), textureRect2.calcY2());
			gl2.glVertex3f(quadRect.getX(), quadRect.calcY2(), 0.0f);
		}
		gl2.glEnd();
	}
	*/
}
