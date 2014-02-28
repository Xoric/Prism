package xoric.prism.scene.renderer;

import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.textures.TextureInfo;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:25:12
 */
public interface IBaseRenderer2
{
	public void reset();

	public void setZ(float z);

	public void flipTextureH(int index);

	public void setupTexture(int index, TextureInfo texInfo);

	public void setupTexture(int index, IFloatRect_r texRect);

	public void setupTexture(int index, IFloatRect_r texRect, boolean flipH);
}
