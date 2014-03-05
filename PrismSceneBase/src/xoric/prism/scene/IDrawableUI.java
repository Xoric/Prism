package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.renderer.IUIRenderer2;

public interface IDrawableUI
{
	public void draw(IUIRenderer2 ren) throws PrismException;
}
