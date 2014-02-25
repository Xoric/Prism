package xoric.prism.scene.shaders;

import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.textures.ITexture;

public interface IDefaultShader extends IBaseShader
{
	public void setTexture(ITexture texture);

	public void setColor(PrismColor color);
}
