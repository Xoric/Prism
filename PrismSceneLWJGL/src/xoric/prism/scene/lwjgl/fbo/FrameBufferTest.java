package xoric.prism.scene.lwjgl.fbo;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

/**
 * @author XoricLee
 * @since 25.02.2014, 20:06:03
 */
public class FrameBufferTest
{
	private int framebufferID;
	private int colorTextureID;
	private int depthRenderBufferID;

	public void test()
	{
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object)
		{
			System.out.println("FBO not supported!!!");
			System.exit(0);
		}
		else
		{

			System.out.println("FBO is supported!!!");

			// init our fbo

			framebufferID = EXTFramebufferObject.glGenFramebuffersEXT(); // create a new framebuffer
			colorTextureID = GL11.glGenTextures(); // and a new texture used as a color buffer
			depthRenderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT(); // And finally a new depthbuffer

			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID); // switch to the new framebuffer

			// initialize color texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID); // Bind the colorbuffer texture
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); // make it linear filterd
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 512, 512, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null); // Create the texture data
			EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
					EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

			// initialize depth renderbuffer
			EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind the depth renderbuffer
			EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, 512, 512); // get the data space for it
			EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
					EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer

			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0); // Swithch back to normal framebuffer rendering

		}
	}
}
