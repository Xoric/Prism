package xoric.prism.creator.drawer.model;

import java.io.File;

import xoric.prism.data.types.IPath_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationLineModel
{
	private IPath_r path;
	private AnimationIndex animationIndex;
	private ViewAngle viewAngle;
	private int tileCount;

	public AnimationLineModel(IPath_r path, AnimationIndex animationIndex, ViewAngle viewAngle)
	{
		this.path = path;
		this.animationIndex = animationIndex;
		this.viewAngle = viewAngle;
	}

	public void load()
	{
		int i = 0;

		while (getFile(i).exists())
			++i;

		tileCount = i;
	}

	public int getCount()
	{
		return tileCount;
	}

	private String getFilename(int index)
	{
		return animationIndex.toString().toLowerCase() + "." + viewAngle.toString().toLowerCase() + "." + index + ".png";
	}

	private File getFile(int index)
	{
		return path.getFile(getFilename(index));
	}
}
