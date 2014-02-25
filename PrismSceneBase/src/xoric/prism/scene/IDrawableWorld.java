package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;

public interface IDrawableWorld
{
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException;
}
