package xoric.prism.scene.lwjgl;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import xoric.prism.data.IFloatPoint_r;
import xoric.prism.scene.IRenderer;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.SceneStage;
import xoric.prism.scene.shader.IShader2;

public class PrismSceneLWJGL implements IScene, IRenderer
{
	private float slope;

	private TextureIO[] textures;

	public PrismSceneLWJGL()
	{
		slope = 0.5f;
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
	public void createWindow(int width, int height, boolean isFullScreen)
	{
		try
		{
			DisplayMode mode = findDisplay(width, height);
			System.out.println("Setting display size to " + mode.getWidth() + " x " + height);
			Display.setDisplayMode(mode);
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
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
	public void startLoop(ISceneListener listener)
	{
		final int loopInterval = IScene.LOOP_INTERVAL_MS;
		long lastMs = System.currentTimeMillis();
		boolean resumeTimer = true;

		//		GL11.glMatrixMode(GL11.GL_PROJECTION);
		//		GL11.glLoadIdentity();
		//		GL11.glOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), 1, -1);
		//		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		/* test */GL11.glMatrixMode(GL11.GL_PROJECTION); // Den richtigen Stack aktivieren
		GL11.glLoadIdentity(); // Die Matrix zur�cksetzen

		//		GL11.glEnable(GL11.GL_DEPTH_TEST); // Tiefentest (mit dem Z-Buffer) aktivieren

		// test
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// enable transparency for png
		GL11.glEnable(GL11.GL_BLEND);
		//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		try
		{
			IShader2 s = ShaderIO2.getInstance().createShader(new File("../debug/defaultShader.vert"),
					new File("../debug/defaultShader.frag"));
			s.activate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//		this.setupQuad();
		try
		{
			textures = new TextureIO[2];
			textures[0] = new TextureIO(new FileInputStream("../debug/g1.png"));
			textures[1] = new TextureIO(new FileInputStream("../debug/g2.png"));
			GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
			textures[0].init();
			textures[1].init();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		int frameCounter = 0;
		int frameTimerMs = 0;

		do
		{
			long currentMs = System.currentTimeMillis();
			int passedMs = (int) (currentMs - lastMs);

			if (passedMs >= loopInterval)
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
				resumeTimer &= listener.requestUpdateScene(passedMs, this);

				// update the scene
				Display.update();
				++frameCounter;
				resumeTimer &= !Display.isCloseRequested();

				// keep track of time passed
				lastMs = currentMs;
			}
			else
			{
				try
				{
					Thread.sleep(loopInterval - passedMs);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					resumeTimer = false;
				}
			}
		}
		while (resumeTimer);

		// clean up (TODO: move to client)
		for (int i = 0; i < textures.length; ++i)
			GL11.glDeleteTextures(textures[i].getProgramID());

		// destroy scene
		listener.onClosingScene();
		Display.destroy();
	}

	@Override
	public void setStage(SceneStage stage)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		switch (stage)
		{
			case GROUND:
				GL11.glFrustum(-0.5, 0.5, -0.5, 0.5, 1.5, 6.5);
				//				GL11.glTranslated(0.0, -0.25, 0.0);
				//				GL11.glScaled(1.5, 1.5, 1.0);
				break;

			case INTERFACE:
				//				GL11.glOrtho(0, width, 0, height, 1, -1);
				GL11.glOrtho(0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f);
				break;
		}
		//		GL11.glScaled(1.0, -1.0, 1.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glLoadIdentity();

		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
	}

	/* **************** IRenderer *************************** */

	@Override
	public void setColor(float r, float g, float b)
	{
		GL11.glColor3f(r, g, b);
	}

	private float calcZ(float y)
	{
		float z = -1.5f - y * slope;
		if (z > -1.499f)
			z = -1.499f;
		return z;
	}

	@Override
	public void drawPlane(IFloatPoint_r position, IFloatPoint_r size)
	{
		bindTexture(textures[0].getProgramID());

		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + position.getX();
		float y0 = position.getY();
		float y = -0.5f + y0;
		float w = size.getX();
		float h = size.getY();
		float zFront = calcZ(y0);
		float zBack = calcZ(y0 + h);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x, y, zFront);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x + w, y, zFront);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x + w, y + h, zBack);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x, y + h, zBack);

		GL11.glEnd();

		unbindTexture();
	}

	@Override
	public void drawObject(IFloatPoint_r position, IFloatPoint_r size, float zOnset)
	{
		bindTexture(textures[1].getProgramID());

		GL11.glBegin(GL11.GL_QUADS);

		float x = -0.5f + position.getX();
		float y0 = position.getY();
		float y = -0.5f + y0;
		float w = size.getX();
		float h = size.getY();
		float z = calcZ(y0) + zOnset;

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x, y, z);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x + w, y, z);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x + w, y + h, z);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x, y + h, z);

		GL11.glEnd();

		unbindTexture();
	}

	@Override
	public void drawSprite(float x, float y, float w, float h)
	{
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + w, y);
		GL11.glVertex2f(x + w, y + h);
		GL11.glVertex2f(x, y + h);

		GL11.glEnd();
	}

	@Override
	public void setSlope(float slope)
	{
		this.slope = slope;
	}

	@Override
	public void bindTexture(int programID)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, programID);
	}

	@Override
	public void unbindTexture()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
