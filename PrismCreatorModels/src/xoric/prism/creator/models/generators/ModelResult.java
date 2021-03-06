package xoric.prism.creator.models.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.world.animations.AnimationIndex;

class ModelResult
{
	private File targetFile;
	private boolean hasPortait;
	private final List<AnimationResult> animations;

	public ModelResult()
	{
		this.targetFile = null;
		this.hasPortait = false;
		this.animations = new ArrayList<AnimationResult>();
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
		for (AnimationResult r : animations)
		{
			if (r.getAnimationIndex() == a)
			{
				r.addVariation();
				return;
			}
		}
		AnimationResult r = new AnimationResult(a);
		animations.add(r);
	}

	public File getTargetFile()
	{
		return targetFile;
	}

	public boolean hasPortrait()
	{
		return hasPortait;
	}

	public List<AnimationResult> getAddedAnimations()
	{
		return animations;
	}

	public boolean hasAnimations()
	{
		return animations.size() > 0;
	}

	public Integer getAnimationCount()
	{
		return animations.size();
	}
}
