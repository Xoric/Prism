package xoric.prism.client.ui;

import xoric.prism.data.types.IFloatRect_r;

public interface IUIChild
{
	public void moveBy(float dx, float dy);

	public void rearrange(IFloatRect_r parentRect);
}
