package xoric.prism.creator.drawer.control;

import xoric.prism.world.entities.AnimationIndex;

public interface IAnimationControl
{
	public void requestAddAnimation(AnimationIndex animation);

	public void requestDeleteAnimation(AnimationIndex animation);
}