package xoric.prism.scene.fbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.scene.lwjgl.ICleanUp;

/**
 * @author XoricLee
 * @since 25.02.2014, 02:37:43
 */
public class FrameBuffer implements IFrameBuffer, ICleanUp
{
	private final IFrameBufferParent parent;
	private final FloatPoint size;

	private int textureID;
	private int renderBufferID;
	private int frameBufferID;

	public FrameBuffer(IFrameBufferParent parent, FloatPoint size)
	{
		this.parent = parent;
		this.size = size;
	}

	public final IFloatPoint_r getSize()
	{
		return size;
	}

	@Override
	public int getTextureID()
	{
		return textureID;
	}

	@Override
	public void init() throws PrismException
	{
		textureID = createTextureObject();
		renderBufferID = createRenderBufferObject();
		frameBufferID = createFrameBufferObject();
	}

	@Override
	public String toString()
	{
		return "frame buffer w=" + (int) size.x + ", h=" + (int) size.y;
	}

	private int createTextureObject()
	{
		IntBuffer textures = IntBuffer.allocate(1);
		GL11.glGenTextures(textures);
		int textureID = textures.get(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, (int) size.x, (int) size.y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) null);

		// TODO experimental
		// gl2.glEnable(GL.GL_BLEND);
		// gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE);
		// .

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		return textureID;
	}

	private int createRenderBufferObject()
	{
		// create a render buffer object to store depth info
		IntBuffer rbo = IntBuffer.allocate(1);
		GL30.glGenRenderbuffers(rbo);
		int renderBufferID = rbo.get(0);

		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBufferID);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, (int) size.x, (int) size.y);

		// TODO experimental
		// gl2.glEnable(GL.GL_BLEND);
		// gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);

		return renderBufferID;
	}

	private int createFrameBufferObject() throws PrismException
	{
		boolean isFboAvailable = GLContext.getCapabilities().GL_EXT_framebuffer_object;

		if (!isFboAvailable)
		{
			PrismException e = new PrismException();
			e.setText(UserErrorText.FRAME_BUFFER_ERROR);
			throw e;
		}

		IntBuffer buffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); // allocate a 1 int byte buffer
		EXTFramebufferObject.glGenFramebuffersEXT(buffer); // generate
		int frameBufferID = buffer.get();

		return frameBufferID;
	}

	@Override
	public void activate()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBufferID);

		//		GL11.glMatrixMode(GL11.GL_PROJECTION);
		//		GL11.glLoadIdentity();
		//		GL11.glOrtho(0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f);
		//		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		//		GL11.glLoadIdentity();
		//		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);

		/* old: 
		GL11.glLoadIdentity();
		GL11.glOrtho(0, SIZE.x, 0, SIZE.y, 0, 1);
		GL11.glPushAttrib(GL2.GL_VIEWPORT_BIT);
		GL11.glViewport(0, 0, SIZE.x, SIZE.y);
		*/

		// TODO experimental
		// gl2.glEnable(GL2.GL_BLEND);
		// gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		// gl2.glBlendFuncSeparate(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA, GL2.GL_ONE,
		// GL2.GL_ONE);
		// .
	}

	@Override
	public void resetToUI()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		parent.setStage(false);
		GL11.glPopAttrib();

		// TODO experimental
		// gl2.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
		// .
	}

	@Override
	public void resetToWorld()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		parent.setStage(true);
		GL11.glPopAttrib();

		// TODO experimental
		// gl2.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
		// .
	}

	// ICleanUp:
	@Override
	public void cleanUp()
	{
		if (textureID > 0)
		{
			GL11.glDeleteTextures(textureID);
			textureID = 0;
		}
		if (renderBufferID > 0)
		{
			GL11.glDeleteTextures(renderBufferID);
			renderBufferID = 0;
		}
		if (frameBufferID > 0)
		{
			GL30.glDeleteFramebuffers(frameBufferID);
			frameBufferID = 0;
		}
	}
}
