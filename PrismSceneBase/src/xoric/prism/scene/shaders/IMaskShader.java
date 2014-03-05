package xoric.prism.scene.shaders;

import xoric.prism.scene.art.ITexture;

public interface IMaskShader extends ITextureShader
{
	public void setMask(ITexture texture);
}
