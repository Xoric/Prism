package xoric.prism.scene;

public interface ISceneListener
{
	/**
	 * Sends a request to update the scene. Returns true if the scene's loop should resume, false otherwise.
	 * @param passedMs
	 * @param renderer
	 * @return
	 */
	public boolean requestUpdateScene(int passedMs, IRenderer renderer);

	public void onClosingScene(Throwable throwable);
}
