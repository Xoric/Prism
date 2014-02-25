package xoric.prism.scene;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.time.IUpdateListener;

public interface ISceneListener extends IInputListener, IUpdateListener
{
	public void drawWorld(IRendererWorld renderer) throws PrismException;

	public void drawUI(IRendererUI renderer) throws PrismException;

	public void onClosingScene(Exception e);
}
