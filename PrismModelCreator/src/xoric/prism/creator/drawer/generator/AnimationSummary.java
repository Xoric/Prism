package xoric.prism.creator.drawer.generator;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IPath_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

class AnimationSummary
{
	private final AnimationIndex animationIndex;
	private final List<AngleSummary> angles;

	public AnimationSummary(AnimationIndex a)
	{
		this.animationIndex = a;
		this.angles = new ArrayList<AngleSummary>();
	}

	public void load(IPath_r path)
	{
		angles.clear();

		for (ViewAngle v : ViewAngle.values())
		{
			AngleSummary an = new AngleSummary(animationIndex, v);
			an.load(path);

			if (an.hasSprites())
				angles.add(an);
		}
	}

	public int countRows()
	{
		return angles.size();
	}

	public int countMaxColumns()
	{
		int max = 0;

		for (AngleSummary as : angles)
		{
			int n = as.getSpriteCount();
			if (n > max)
				max = n;
		}
		return max;
	}

	public boolean hasSprites()
	{
		return angles.size() > 0;
	}

	public int getAngleCount()
	{
		return angles.size();
	}

	public AngleSummary getAngle(int index)
	{
		return angles.get(index);
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}
}
