package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;
import xoric.prism.scene.renderer.IUIRenderer2;
import xoric.prism.scene.renderer.IWorldRenderer2;

public interface ISceneListener extends IInputListener, IUpdateListener
{
	public void drawWorld(IWorldRenderer2 ren) throws PrismException;

	public void drawUI(IUIRenderer2 ren) throws PrismException;

	public void onClosingScene(Exception e);
}
