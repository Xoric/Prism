package xoric.prism.creator.drawer.model;

import java.io.File;

import xoric.prism.data.types.IPath_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationAngleModel
{
	private IPath_r path;
	private AnimationIndex animationIndex;
	private ViewAngle viewAngle;

	private int tileCount;

	public AnimationAngleModel(IPath_r path, AnimationIndex a, ViewAngle v)
	{
		this.path = path;
		this.animationIndex = a;
		this.viewAngle = v;
	}

	public void load()
	{
		int i = 0;

		while (getFile(i).exists())
			++i;

		tileCount = i;
	}

	public boolean isUsed()
	{
		return tileCount > 0;
	}

	public int getCount()
	{
		return tileCount;
	}

	private String getFilename(int index)
	{
		return SpriteNames.getFilename(animationIndex, viewAngle, index);
	}

	private File getFile(int index)
	{
		return path.getFile(getFilename(index));
	}
}