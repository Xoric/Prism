package xoric.prism.creator.drawer.control;

import xoric.prism.world.animations.AnimationIndex;

public interface IAnimationControl
{
	public void requestAddAnimation(AnimationIndex animation);

	public void requestDeleteAnimation(AnimationIndex animation);

	public void requestSetAnimationDuration(AnimationIndex animation, int ms);
}
