package xoric.prism.creator.drawer.control;

import java.io.File;
import java.util.List;

import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public interface ISpriteControl
{
	public void requestAddSprite(AnimationIndex animation, ViewAngle v, int index);

	public void requestDeleteSprites(AnimationIndex animation, ViewAngle v, List<Integer> indices);

	public void requestEditSprite(File file);

	public void requestInputExternalImageEditor();
}
