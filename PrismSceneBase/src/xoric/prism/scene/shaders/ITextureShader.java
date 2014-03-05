package xoric.prism.scene.shaders;

import xoric.prism.scene.art.ITexture;

/**
 * @author XoricLee
 * @since 26.02.2014, 13:56:33
 */
public interface ITextureShader extends IBaseShader
{
	public void setTexture(ITexture texture);

	public void setTexture(int textureID);
}
