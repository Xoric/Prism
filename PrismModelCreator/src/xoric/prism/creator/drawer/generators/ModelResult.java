package xoric.prism.creator.drawer.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.world.animations.AnimationIndex;

class ModelResult
{
	private File targetFile;
	private boolean hasPortait;
	private final List<AnimationIndex> animations;

	public ModelResult()
	{
		this.targetFile = null;
		this.hasPortait = false;
		this.animations = new ArrayList<AnimationIndex>();
	}

	public void setTargetFile(File targetFile)
	{
		this.targetFile = targetFile;
	}

	public void setPortrait(boolean hasPortrait)
	{
		this.hasPortait = hasPortrait;
	}

	public void addAnimation(AnimationIndex a)
	{
		animations.add(a);
	}

	public File getTargetFile()
	{
		return targetFile;
	}

	public boolean hasPortrait()
	{
		return hasPortait;
	}

	public List<AnimationIndex> getAddedAnimations()
	{
		return animations;
	}

	public boolean hasAnimations()
	{
		return animations.size() > 0;
	}
}