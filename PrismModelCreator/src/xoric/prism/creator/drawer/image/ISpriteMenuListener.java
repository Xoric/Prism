package xoric.prism.creator.drawer.image;

public interface ISpriteMenuListener
{
	public void requestCloneSprite();

	public void requestInsertSprite();

	public void requestInsertSpriteFromClipboard();

	public void requestEditSprite();

	public void requestDeleteSprites();

	public void requestCopySpriteImage();

	public void requestCopySpriteFilename();

	public void requestMakeTransparent();

	public void requestReloadSprite();
}
