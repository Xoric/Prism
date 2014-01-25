package xoric.prism.creator.models.control;

import java.io.File;
import java.util.List;

import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public interface ISpriteControl
{
	public void requestCloneSprite(AnimationIndex a, int variation, ViewAngle v, int index);

	public void requestInsertSprite(AnimationIndex a, int variation, ViewAngle v, int index);

	public void requestInsertSpriteFromClipboard(AnimationIndex a, int variation, ViewAngle v, int index);

	public void requestCopySpriteToClipboard(AnimationIndex a, int variation, ViewAngle v, int index);

	public void requestDeleteSprites(AnimationIndex a, int variation, ViewAngle v, List<Integer> indices);

	public void requestEditSprite(File file);

	public void requestMakeSpritesTransparent(AnimationIndex a, int variation, ViewAngle v, List<Integer> indices);
}
