package xoric.prism.creator.ground.model;

import xoric.prism.data.types.IPath_r;

/**
 * @author XoricLee
 * @since 22.02.2014, 20:19:52
 */
public class GroundModel
{
	private final IPath_r path;

	public GroundModel(IPath_r path)
	{
		this.path = path;
	}

	public IPath_r getPath()
	{
		return path;
	}
}
