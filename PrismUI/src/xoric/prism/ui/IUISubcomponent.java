package xoric.prism.ui;

import xoric.prism.data.types.IFloatRect_r;

public interface IUISubcomponent
{
	public void moveBy(float dx, float dy);

	public void rearrange(IFloatRect_r parentRect);
}
