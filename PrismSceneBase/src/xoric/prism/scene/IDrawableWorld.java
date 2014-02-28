package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.camera.ICameraTransform;

public interface IDrawableWorld
{
	public void draw(IRendererWorld renderer, ICameraTransform cam) throws PrismException;
}
