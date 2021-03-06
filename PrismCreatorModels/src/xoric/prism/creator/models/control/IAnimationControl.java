package xoric.prism.creator.models.control;

import xoric.prism.world.animations.AnimationIndex;

public interface IAnimationControl
{
	public void requestAddAnimation(AnimationIndex a);

	public void requestDeleteAnimation(AnimationIndex a, int variation);

	/**
	 * Requests the deletion of a given animation. The request can either be the deletion of a single variation {@code (variation >= 0)} or
	 * all variations {@code (variation < 0)}.
	 * @param a
	 * @param variation
	 * @param ms
	 */
	public void requestSetAnimationDuration(AnimationIndex a, int variation, int ms);
}
