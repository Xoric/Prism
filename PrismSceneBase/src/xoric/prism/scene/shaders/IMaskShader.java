package xoric.prism.scene.shaders;

import xoric.prism.scene.textures.ITexture;

public interface IMaskShader extends IDefaultShader
{
	public void setMask(ITexture texture);
}
