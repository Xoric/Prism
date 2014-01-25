package xoric.prism.creator.drawer.view;

import xoric.prism.world.animations.AnimationIndex;

public interface IAnimationEditor
{
	public void requestEditAnimation(AnimationIndex animation);

	public void requestCloseAnimationEditor();
}
