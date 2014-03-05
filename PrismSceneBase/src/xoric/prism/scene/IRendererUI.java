package xoric.prism.scene;

import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.art.TextureInfo;

@Deprecated
public interface IRendererUI
{
	@Deprecated
	public void drawSprite(TextureInfo texInfo, IFloatRect_r screenRect);

	@Deprecated
	public void drawSprite(IFloatRect_r texRect, IFloatRect_r screenRect);
}
