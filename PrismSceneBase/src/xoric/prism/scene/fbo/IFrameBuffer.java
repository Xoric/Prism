package xoric.prism.scene.fbo;

import xoric.prism.data.exceptions.PrismException;

/**
 * @author XoricLee
 * @since 25.02.2014, 02:40:17
 */
public interface IFrameBuffer
{
	public void init() throws PrismException;

	public int getTextureID();

	public void activate();

	public void resetToUI();

	public void resetToWorld();
}
