package xoric.prism.scene.shaders;

import xoric.prism.scene.textures.ITexture;

public interface IMaskShader extends ITextureShader
{
	public void setMask(ITexture texture);
}
