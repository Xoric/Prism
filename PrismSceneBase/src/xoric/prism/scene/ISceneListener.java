package xoric.prism.scene;

import xoric.prism.data.time.IUpdateListener;

public interface ISceneListener extends IInputListener, IUpdateListener
{
	public boolean drawWorld(IRendererWorld renderer) throws Exception;

	public boolean drawUI(IRendererUI renderer) throws Exception;

	public void onClosingScene(Exception e);
}
