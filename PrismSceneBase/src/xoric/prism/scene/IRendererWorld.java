package xoric.prism.scene;

import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.textures.TextureInfo;

public interface IRendererWorld
{
	public void drawPlane(TextureInfo texInfo, IFloatRect_r screenRect);

	public void drawPlane(IFloatRect_r texRect, IFloatRect_r screenRect);

	public void drawMaskPlane(IFloatRect_r texRect, IFloatRect_r maskRect, IFloatRect_r screenRect);

	public void drawObject(TextureInfo texInfo, IFloatPoint_r position, IFloatPoint_r size, float z);
}
