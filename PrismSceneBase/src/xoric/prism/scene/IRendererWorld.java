package xoric.prism.scene;

import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.art.TextureInfo;

@Deprecated
public interface IRendererWorld
{
	@Deprecated
	public void drawPlane(TextureInfo texInfo, IFloatRect_r screenRect);

	@Deprecated
	public void drawPlane(IFloatRect_r texRect, IFloatRect_r screenRect, float z);

	@Deprecated
	public void drawMaskPlane(IFloatRect_r texRect, IFloatRect_r maskRect, IFloatRect_r screenRect);

	@Deprecated
	public void drawMask2Plane(IFloatRect_r texRect, IFloatRect_r mask1Rect, IFloatRect_r mask2Rect, IFloatRect_r screenRect);

	@Deprecated
	public void drawObject(TextureInfo texInfo, IFloatPoint_r position, IFloatPoint_r size, float z);

	@Deprecated
	public void drawObject(TextureInfo texInfo, TextureInfo maskInfo, IFloatPoint_r position, IFloatPoint_r size);
}
