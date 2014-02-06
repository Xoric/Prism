package xoric.prism.scene;


public interface ISceneListener extends IInputListener
{
	public boolean drawWorld(int passedMs, IRendererWorld renderer) throws Exception;

	public boolean drawUI(int passedMs, IRendererUI renderer) throws Exception;

	public void onClosingScene(Exception e);
}
