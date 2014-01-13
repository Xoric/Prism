package xoric.prism.creator.drawer.model;

import java.io.File;

import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationModel
{
	private IPath_r path;
	private AnimationIndex animationIndex;
	private AnimationAngleModel[] angles;
	private int durationMs;
	private boolean isUnlocked;

	public AnimationModel(IPath_r path, AnimationIndex a)
	{
		this.path = path;
		this.animationIndex = a;
		this.durationMs = 1000;
		this.isUnlocked = false;

		int n = ViewAngle.values().length;
		this.angles = new AnimationAngleModel[n];
		for (ViewAngle v : ViewAngle.values())
			angles[v.ordinal()] = new AnimationAngleModel(path, a, v);
	}

	public IPath_r getPath()
	{
		return path;
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}

	public void unlock()
	{
		this.isUnlocked = true;
	}

	public boolean isUsed()
	{
		boolean b = isUnlocked;

		if (!b)
			for (AnimationAngleModel m : angles)
				b |= m.isUsed();

		return b;
	}

	public void load()
	{
		for (AnimationAngleModel a : angles)
			a.load();
	}

	public File getFile(ViewAngle v, int index)
	{
		return path.getFile(getFilename(v, index));
	}

	public String getFilename(ViewAngle v, int index)
	{
		return SpriteNames.getFilename(animationIndex, v, index);
	}

	public void setDurationMs(int ms)
	{
		durationMs = ms;
	}

	public int getDurationMs()
	{
		return durationMs;
	}
}
