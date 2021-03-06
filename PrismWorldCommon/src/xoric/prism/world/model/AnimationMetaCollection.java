package xoric.prism.world.model;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.world.animations.AnimationIndex;

public class AnimationMetaCollection
{
	private final AnimationIndex animation;
	private final List<AnimationMeta> variations;
	private int currentIndex;

	public AnimationMetaCollection(AnimationIndex a)
	{
		this.animation = a;
		this.variations = new ArrayList<AnimationMeta>();
	}

	public AnimationIndex getAnimationIndex()
	{
		return animation;
	}

	public void addVariation(AnimationMeta am)
	{
		this.variations.add(am);
	}

	public int getVariationCount()
	{
		return variations.size();
	}

	public AnimationMeta getVariation(int index)
	{
		return variations.get(index);
	}

	public AnimationMeta getAnyVariation()
	{
		AnimationMeta am = variations.get(currentIndex);

		if (++currentIndex >= variations.size())
			currentIndex = 0;

		return am;
	}
}
