package xoric.prism.scene.lwjgl;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

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
import xoric.prism.scene.lwjgl.textures.Texture;
import xoric.prism.scene.lwjgl.textures.TextureBinderLWJGL;
import xoric.prism.scene.settings.ISceneSettings;
import xoric.prism.scene.shaders.AllShaders;
import xoric.prism.scene.shaders.IShader2;
import xoric.prism.scene.textures.TextureInfo;

public class PrismSceneLWJGL implements IScene, IRendererWorld, IRendererUI
{
	private final InputHandlerLWJGL inputHandler;

	private final ShaderIO2 shaderIO;
	private FloatPoint screenSize;
	private Exception exception;

	private float slope;

	private TextureIO[] textures;
	private IShader2 testingDefaultShader;
	private Texture testingTexture;
	private PrismColor testingColor2;
	private boolean[] brgba = new boolean[4];

	private int interval;

	public PrismSceneLWJGL()
	{
		setFps(60);

		inputHandler = new InputHandlerLWJGL();

		shaderIO = new ShaderIO2();
		slope = 0.5f;
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
			screenSize = new FloatPoint(mode.getWidth(), mode.getHeight());
			Display.setDisplayMode(mode);
			Display.create();
		}
		catch (LWJGLException e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("An error occured while trying to create a display window.");
			throw e;
		}
		return screenSize;
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

	private void memorizeException(Exception e)
	{
		this.exception = e;
	}

	@Override
	public void initialize()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);

		//		GL11.glEnable(GL11.GL_DEPTH_TEST); // Tiefentest (mit dem Z-Buffer) aktivieren

		try
		{
			AllShaders.load(this);
		}
		catch (Exception e2)
		{
			memorizeException(e2);
		}

		try
		{
			// TODO: throws an exception on netbook
			testingDefaultShader = ShaderIO2.createShader(new File("../debug/defaultShader.vert"), new File("../debug/defaultShader.frag"));
			testingDefaultShader.activate();
		}
		catch (PrismException e)
		{
			e.code.print();
		}

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
	}

	@Override
	public void startLoop(ISceneListener client, IInputListener il)
	{
		inputHandler.initialize(il, (int) screenSize.y);

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
			inputHandler.update();

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
					try
					{
						if (resumeTimer)
						{
							setStage(true);
							resumeTimer = client.drawWorld(passedMs, this);
						}
						if (resumeTimer)
						{
							setStage(false);
							resumeTimer = client.drawUI(passedMs, this);
						}
					}
					catch (Exception e0)
					{
						resumeTimer = false;
						exception = e0;
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
					if (exception == null)
						exception = e;
				}
			}

			resumeTimer &= exception == null;
		}
		while (resumeTimer);

		// clean up (TODO: move to client)
		if (textures != null)
			for (int i = 0; i < textures.length; ++i)
				GL11.glDeleteTextures(textures[i].getProgramID());

		// destroy scene
		Display.destroy();
		client.onClosingScene(exception);
	}

	private void setStage(boolean isWorldStage)
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

	private float calcZ(float y)
	{
		float z = -1.5f - y * slope;
		if (z > -1.499f)
			z = -1.499f;

		return z;
	}

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
	public IShader2 createShader(ByteBuffer vertexShader, ByteBuffer pixelShader) throws PrismException
	{
		return ShaderIO2.createShader(vertexShader, pixelShader);
	}

	@Override
	public void drawSprite(TextureInfo texInfo, IFloatRect_r rect)
	{
		GL11.glBegin(GL11.GL_QUADS);

		float x = rect.getTopLeft().getX() / screenSize.x;
		float y = 1.0f - rect.getTopLeft().getY() / screenSize.y;
		float w = rect.getSize().getX() / screenSize.x;
		float h = -(rect.getSize().getY() / screenSize.y);

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
		float x = screenRect.getTopLeft().getX() / screenSize.x;
		float y = 1.0f - screenRect.getTopLeft().getY() / screenSize.y;
		float w = screenRect.getSize().getX() / screenSize.x;
		float h = -screenRect.getSize().getY() / screenSize.y;

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
	public void drawPlane(TextureInfo texInfo, IFloatRect_r rect)
	{
		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + rect.getTopLeft().getX();
		float y0 = 1.0f - rect.getTopLeft().getY();
		float y = -0.5f + y0;
		float w = rect.getSize().getX();
		float h = -rect.getSize().getY();
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
	public void drawObject(TextureInfo texInfo, IFloatPoint_r position, IFloatPoint_r size, float z)
	{
		//		float[] rgba = testingColor2.getRGBA();
		//		for (int i = 0; i < 3; ++i)
		//		{
		//			if (brgba[i])
		//			{
		//				rgba[i] += 0.001f * i;
		//				if (rgba[i] > 1.0f)
		//				{
		//					rgba[i] = 1.0f;
		//					brgba[i] = false;
		//				}
		//			}
		//			else
		//			{
		//				rgba[i] -= 0.001f * i;
		//				if (rgba[i] < 0.0f)
		//				{
		//					rgba[i] = 0.0f;
		//					brgba[i] = true;
		//				}
		//			}
		//		}

		//		bindTexture(textures[0].getProgramID(), false);
		//		testingDefaultShader.activate();
		//		testingDefaultShader.setTexture(testingTexture);
		//		testingDefaultShader.setColor(testingColor2);

		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + position.getX();
		float y0 = position.getY();
		float y = -0.5f + y0;
		float w = size.getX();
		float h = size.getY();
		z = calcZ(y0) + z;

		//		GL11.glTexCoord2f(0.0f, 0.0f);
		//		GL11.glVertex3f(x, y, z);
		//		GL11.glTexCoord2f(1.0f, 0.0f);
		//		GL11.glVertex3f(x + w, y, z);
		//		GL11.glTexCoord2f(1.0f, 1.0f);
		//		GL11.glVertex3f(x + w, y + h, z);
		//		GL11.glTexCoord2f(0.0f, 1.0f);
		//		GL11.glVertex3f(x, y + h, z);

		IFloatPoint_r tl = texInfo.getRect().getTopLeft();
		IFloatPoint_r br = texInfo.getRect().getBottomRight();

		if (texInfo.isFlippedH())
		{
			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x, y, z);
			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x + w, y, z);
			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x + w, y + h, z);
			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x, y + h, z);
		}
		else
		{
			GL11.glTexCoord2f(tl.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x, y, z);
			GL11.glTexCoord2f(br.getX(), 1.0f - tl.getY());
			GL11.glVertex3f(x + w, y, z);
			GL11.glTexCoord2f(br.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x + w, y + h, z);
			GL11.glTexCoord2f(tl.getX(), 1.0f - br.getY());
			GL11.glVertex3f(x, y + h, z);
		}
		GL11.glEnd();
	}

	@Override
	public IFloatPoint_r getScreenSize()
	{
		return screenSize;
	}

	@Override
	public void setWindowTitle(String title)
	{
		Display.setTitle(title);
	}
}
