package xoric.prism.scene.fbo;

import xoric.prism.data.types.FloatPoint;

/**
 * @author XoricLee
 * @since 25.02.2014, 12:20:20
 */
public interface IFrameBufferIO
{
	public IFrameBuffer createFrameBuffer(FloatPoint size);
}
