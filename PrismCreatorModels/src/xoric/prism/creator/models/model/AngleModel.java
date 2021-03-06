package xoric.prism.creator.models.model;

import java.io.File;

import xoric.prism.creator.models.image.AnimationSpriteNameGenerator;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AngleModel
{
	private IPath_r path;
	private AnimationIndex animationIndex;
	private int variation;
	private ViewAngle viewAngle;

	private int tileCount;

	public AngleModel(IPath_r path, AnimationIndex a, int variation, ViewAngle v)
	{
		this.path = path;
		this.animationIndex = a;
		this.variation = variation;
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
		return AnimationSpriteNameGenerator.getFilename(animationIndex, variation, viewAngle, index);
	}

	private File getFile(int index)
	{
		return path.getFile(getFilename(index));
	}
}
