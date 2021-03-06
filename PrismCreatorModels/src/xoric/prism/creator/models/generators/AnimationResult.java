package xoric.prism.creator.models.generators;

import xoric.prism.world.animations.AnimationIndex;

class AnimationResult
{
	private final AnimationIndex animationIndex;
	private int variations;

	public AnimationResult(AnimationIndex animationIndex)
	{
		this.animationIndex = animationIndex;
		this.variations = 1;
	}

	public void addVariation()
	{
		++variations;
	}

	public int getVariationCount()
	{
		return variations;
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}

	@Override
	public String toString()
	{
		String s;
		if (variations > 1)
			s = animationIndex.toString() + " (" + variations + ")";
		else
			s = animationIndex.toString();

		return s;
	}
}
