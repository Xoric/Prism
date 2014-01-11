package xoric.prism.creator.drawer.control;

import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public interface ISpriteControl
{
	public void requestAddSprite(AnimationIndex animation, ViewAngle v, int index);
}
