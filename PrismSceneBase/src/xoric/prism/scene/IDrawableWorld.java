package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.renderer.IWorldRenderer2;

public interface IDrawableWorld
{
	public void draw(IWorldRenderer2 ren) throws PrismException;
}
