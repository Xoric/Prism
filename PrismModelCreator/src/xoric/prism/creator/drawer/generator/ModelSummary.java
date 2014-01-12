package xoric.prism.creator.drawer.generator;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IPath_r;
import xoric.prism.world.entities.AnimationIndex;

class ModelSummary
{
	private final List<AnimationSummary> animations;

	public ModelSummary()
	{
		this.animations = new ArrayList<AnimationSummary>();
	}

	public void load(IPath_r path)
	{
		animations.clear();

		for (AnimationIndex a : AnimationIndex.values())
		{
			AnimationSummary as = new AnimationSummary(a);
			as.load(path);

			if (as.hasSprites())
				animations.add(as);
		}
	}

	public int countRows()
	{
		int n = 0;

		for (AnimationSummary as : animations)
			n += as.countRows();

		return n;
	}

	public int countColumns()
	{
		int max = 0;

		for (AnimationSummary as : animations)
		{
			int n = as.countMaxColumns();
			if (n > max)
				max = n;
		}
		return max;
	}

	public AnimationSummary getAnimation(int index)
	{
		return animations.get(index);
	}

	public int getAnimationCount()
	{
		return animations.size();
	}

	public boolean hasAnimations()
	{
		return animations.size() > 0;
	}
}