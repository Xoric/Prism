package xoric.prism.client.settings;

import xoric.prism.scene.settings.ISceneSettings;

public class ClientSettings implements ISceneSettings
{
	private int fps;

	public ClientSettings()
	{
		fps = 90;
	}

	@Override
	public int getFps()
	{
		return fps;
	}

	public void setFps(int fps)
	{
		this.fps = fps;
	}
}
