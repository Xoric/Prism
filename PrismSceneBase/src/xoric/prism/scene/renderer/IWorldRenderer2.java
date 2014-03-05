package xoric.prism.scene.renderer;

import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.camera.ICameraTransform;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:24:50
 */
public interface IWorldRenderer2 extends IBaseRenderer2
{
	public void setCamera(ICameraTransform cam);

	public void setSprite(IFloatPoint_r screenPos, IFloatPoint_r spriteSize);

	public void setSprite(IFloatRect_r spriteRect);

	public void drawPlane(int texCount);

	public void drawObject(int texCount);
}
