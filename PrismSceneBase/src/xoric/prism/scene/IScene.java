package xoric.prism.scene;

import java.awt.Dimension;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.scene.settings.ISceneSettings;
import xoric.prism.scene.shaders.IShaderIO;

public interface IScene extends IShaderIO
{
	/**
	 * Returns a list of available resolutions.
	 * @return List<Dimension>
	 */
	public List<Dimension> getAvailableResolutions();

	public IPoint_r findBestResolution(int width, int height);

	/**
	 * Creates a window with the desired resolution. Returns the actual resolution of the window.
	 * @param width
	 * @param height
	 * @param isFullScreen
	 * @return IFloatPoint_r
	 * @throws PrismException
	 */
	public IFloatPoint_r createWindow(int width, int height, boolean isFullScreen) throws PrismException;

	public IFloatPoint_r getScreenSize();

	public void loadSettings(ISceneSettings settings);

	/**
	 * Sets the window to fullscreen.
	 * @param fullscreen
	 */
	public void setFullScreen(boolean fullscreen);

	public void initialize();

	public void startLoop(ISceneListener sceneListener, IInputListener inputListener);

	public void setSlope(float slope);

	public void setWindowTitle(String title);
}
