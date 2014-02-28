package xoric.prism.scene.fbo;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatRect_r;

/**
 * @author XoricLee
 * @since 25.02.2014, 02:40:17
 */
public interface IFrameBuffer
{
	public void initialize() throws PrismException;

	public int getTextureID();

	public IFloatRect_r getRect();

	public void activate();

	public void resetToUI();

	public void resetToWorld();

	@Deprecated
	public void drawBlue();

	@Deprecated
	public void drawCustom();

	public void drawSprite(IFloatRect_r texRect);

	@Deprecated
	public void renderScreen();
}
