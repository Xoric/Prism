package xoric.prism.creator.drawer.generators;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;

class AnimationSummary
{
	private final AnimationIndex animationIndex;
	private final List<VariationSummary> variations;

	public AnimationSummary(AnimationIndex a)
	{
		this.animationIndex = a;
		this.variations = new ArrayList<VariationSummary>();
	}

	public void load(IPath_r path)
	{
		variations.clear();

		int variation = 0;
		boolean b;
		do
		{
			VariationSummary vs = new VariationSummary(animationIndex, variation);
			vs.load(path);
			b = vs.hasSprites();

			++variation;
			if (b)
				variations.add(vs);
		}
		while (b);
	}

	public int countRows()
	{
		int n = 0;

		for (VariationSummary vs : variations)
			n += vs.countRows();

		return n;
	}

	public int getVariationCount()
	{
		return variations.size();
	}

	public int countMaxColumns()
	{
		int max = 0;

		for (VariationSummary vs : variations)
		{
			int n = vs.countMaxColumns();
			if (n > max)
				max = n;
		}
		return max;
	}

	public boolean isUsed()
	{
		return variations.size() > 0;
	}

	public VariationSummary getVariation(int index)
	{
		return variations.get(index);
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}

	public List<VariationSummary> getVariations()
	{
		return variations;
	}

	public boolean hasVariations()
	{
		return variations.size() > 0;
	}
}
