package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;

public interface IDrawableUI
{
	public void draw(IRendererUI renderer) throws PrismException;
}
