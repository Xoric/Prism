package xoric.prism.creator.drawer.model;

import xoric.prism.data.types.IPath_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationModel
{
	private IPath_r path;
	private AnimationIndex animationIndex;
	private AnimationLineModel directions[];
	private boolean isUsed;

	public AnimationModel(IPath_r path, AnimationIndex animationIndex)
	{
		this.path = path;
		this.animationIndex = animationIndex;

		int n = ViewAngle.values().length;
		this.directions = new AnimationLineModel[n];

		for (ViewAngle a : ViewAngle.values())
			directions[a.ordinal()] = new AnimationLineModel(path, animationIndex, a);
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}

	public boolean isUsed()
	{
		return isUsed;
	}

	public void load()
	{
		isUsed = false;

		for (AnimationLineModel a : directions)
		{
			a.load();
			isUsed |= a.getCount() > 0;
		}
	}
}
