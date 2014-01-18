package xoric.prism.scene;

import java.awt.Dimension;
import java.util.List;

public interface IScene
{
	public static final int LOOP_INTERVAL_MS = 25;

	/**
	 * Returns a list of available resolutions.
	 * @return List<Dimension>
	 */
	public List<Dimension> getAvailableResolutions();

	/**
	 * Creates a window with the given resolution.
	 * @param width
	 * @param height
	 * @param isFullScreen
	 */
	public void createWindow(int width, int height, boolean isFullScreen);

	/**
	 * Sets the window to fullscreen.
	 * @param fullscreen
	 */
	public void setFullScreen(boolean fullscreen);

	public void initialize();

	public void startLoop(ISceneListener listener);

	public void setStage(SceneStage stage);

	public void setSlope(float slope);
}
