package xoric.prism.scene.renderer;

import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:24:50
 */
public interface IUIRenderer2 extends IBaseRenderer2
{
	public void setScreenSize(IFloatPoint_r size);

	public void setupSprite(IFloatRect_r screenRect);

	public void drawSprite(int texCount);
}
