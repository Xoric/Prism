package xoric.prism.scene.materials.fbo;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.fbo.IFrameBuffer;
import xoric.prism.scene.fbo.IFrameBufferIO;

/**
 * @author XoricLee
 * @since 25.02.2014, 17:44:54
 */
public abstract class AllBuffers
{
	public static IFrameBuffer groundBuffer;

	public static void loadAll(IFrameBufferIO frameBufferIO) throws PrismException
	{
		//		groundBuffer = frameBufferIO.createFrameBuffer(new FloatPoint(120.0f, 80.0f));
	}
}
