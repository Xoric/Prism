package xoric.prism.scene.shaders;

import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.textures.ITexture;

public interface IShader2
{
	public void activate();

	public void setTexture(ITexture texture);

	public void setColor(PrismColor color);
}
