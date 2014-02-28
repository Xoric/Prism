package xoric.prism.scene;

import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.textures.TextureInfo;

public interface IRendererUI
{
	public void drawSprite(TextureInfo texInfo, IFloatRect_r screenRect);

	public void drawSprite(IFloatRect_r texRect, IFloatRect_r screenRect);
}
