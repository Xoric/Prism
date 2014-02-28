package xoric.prism.scene.lwjgl.fbo;

import java.awt.Point;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.fbo.IFrameBuffer;
import xoric.prism.scene.lwjgl.ICleanUp;

/**
 * @author XoricLee
 * @since 15.09.2011, 00:34:48
 */
public class FrameBuffer2 implements IFrameBuffer, ICleanUp
{
	private final IFrameBufferParent parent;

	public static final Point SIZE = new Point(120, 80);
	private static final FloatPoint SIZE_FLOAT = new FloatPoint(SIZE.x, SIZE.y);
	public static final FloatRect TEXTURE_RECT = new FloatRect(0.0f, 0.0f, 1.0f, 1.0f);

	private int fboId;
	private int textureId;
	private int rboId;

	public FrameBuffer2(IFrameBufferParent parent, FloatPoint size)
	{
		this.parent = parent;
	}

	// IFrameBuffer:
	@Override
	public int getTextureID()
	{
		return textureId;
	}

	public static final IFloatPoint_r getSize()
	{
		return SIZE_FLOAT;
	}

	public IFloatRect_r getSourceTextureRect()
	{
		return TEXTURE_RECT;
	}

	@Override
	public void activate()
	{
		setActive(true);
	}

	private void setActive(boolean active)
	{
		if (active)
		{
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, SIZE.x, 0, SIZE.y, 0, 1);
			GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
			GL11.glViewport(0, 0, SIZE.x, SIZE.y);

			// TODO experimental
			// gl2.glEnable(GL2.GL_BLEND);
			// gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			// gl2.glBlendFuncSeparate(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA, GL2.GL_ONE,
			// GL2.GL_ONE);
			// .
		}
		else
		{
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
			parent.resetOrthoExperimental();
			GL11.glPopAttrib();

			// TODO experimental
			// gl2.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
			// .
		}
	}

	public int getTextureId()
	{
		return textureId;
	}

	@Override
	public void initialize() throws PrismException
	{
		createTextureObject();
		createRenderBufferObject();
		createFrameBufferObject();

		// Does the GPU support current FBO configuration?
		// TODO status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
		// if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
	}

	private void createTextureObject()
	{
		textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, SIZE.x, SIZE.y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);

		// TODO experimental
		// gl2.glEnable(GL.GL_BLEND);
		// gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE);
		// .

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private void createRenderBufferObject()
	{
		// create a render buffer object to store depth info
		rboId = GL30.glGenRenderbuffers();

		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rboId);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, SIZE.x, SIZE.y);

		// TODO experimental
		// gl2.glEnable(GL.GL_BLEND);
		// gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
	}

	private void createFrameBufferObject()
	{
		// create a frame buffer object
		fboId = GL30.glGenFramebuffers();
		setActive(true);

		// attach the texture to FBO color attachment point
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureId, 0);

		// TODO experimental
		// gl2.glEnable(GL2.GL_BLEND);

		// deactivate
		setActive(false);
	}

	@Override
	public void cleanUp()
	{
		if (fboId > 0)
		{
			GL30.glDeleteFramebuffers(fboId);
			fboId = 0;
		}
	}

	// IFrameBuffer:
	@Override
	public IFloatRect_r getRect()
	{
		// TODO Auto-generated method stub
		return TEXTURE_RECT;
	}

	// IFrameBuffer:
	@Override
	public void resetToUI()
	{
		setActive(false);
		parent.setStage(true);
	}

	// IFrameBuffer:
	@Override
	public void resetToWorld()
	{
		setActive(false);
		parent.setStage(true);
	}

	// IFrameBuffer:
	@Override
	public void drawBlue()
	{
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	// IFrameBuffer:
	@Override
	public void drawCustom()
	{
		GL11.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	// IFrameBuffer:
	@Override
	public void drawSprite(IFloatRect_r texRect)
	{
		// TODO Auto-generated method stub

	}

	// IFrameBuffer:
	@Override
	public void renderScreen()
	{
		// TODO Auto-generated method stub

	}
}
