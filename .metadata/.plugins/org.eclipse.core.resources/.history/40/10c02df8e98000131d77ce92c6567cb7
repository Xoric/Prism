package xoric.prism.creator.drawer.control;

import java.io.File;
import java.util.List;

import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public interface ISpriteControl
{
	public void requestCloneSprite(AnimationIndex a, ViewAngle v, int index);

	public void requestInsertSprite(AnimationIndex a, ViewAngle v, int index);

	public void requestInsertSpriteFromClipboard(AnimationIndex a, ViewAngle v, int index);

	public void requestCopySpriteToClipboard(AnimationIndex a, ViewAngle v, int index);

	public void requestDeleteSprites(AnimationIndex a, ViewAngle v, List<Integer> indices);

	public void requestEditSprite(File file);

	public void requestMakeSpritesTransparent(AnimationIndex a, ViewAngle v, List<Integer> indices);

	public void requestInputExternalImageEditor();
}
