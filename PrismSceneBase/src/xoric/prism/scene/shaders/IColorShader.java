package xoric.prism.scene.shaders;

import xoric.prism.data.types.PrismColor;

public interface IColorShader extends ITextureShader
{
	public void setColor(PrismColor color);
}
