package xoric.prism.creator.drawer.view;

import xoric.prism.world.entities.AnimationIndex;

public interface IAnimationEditor
{
	public void requestEditAnimation(AnimationIndex animation);

	public void requestCloseAnimationEditor();
}