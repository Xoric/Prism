package xoric.prism.creator.common.spritelist.control;

import java.io.File;

import xoric.prism.data.types.IPath_r;

public class SpriteNameGenerator
{
	private final IPath_r path;
	private final String prefix;
	private final String suffix;

	public SpriteNameGenerator(IPath_r path, String prefix, String suffix)
	{
		this.path = path;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public String getFilename(int index)
	{
		return prefix + index + suffix;
	}

	public final File getFile(String filename)
	{
		return path.getFile(filename);
	}

	public final File getFile(int index)
	{
		return path.getFile(getFilename(index));
	}

	public final IPath_r getPath()
	{
		return path;
	}
}
