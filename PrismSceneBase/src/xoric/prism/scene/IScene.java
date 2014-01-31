package xoric.prism.scene;

import java.awt.Dimension;
import java.util.List;

import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.scene.shaders.IShaderIO;

public interface IScene extends IShaderIO
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

	public IFloatPoint_r getScreenSize();

	/**
	 * Sets the window to fullscreen.
	 * @param fullscreen
	 */
	public void setFullScreen(boolean fullscreen);

	public void initialize();

	public void startLoop(ISceneListener listener);

	public void setSlope(float slope);
}
