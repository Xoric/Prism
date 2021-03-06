package xoric.prism.creator.common.spritelist.view;

public interface ISpriteMenuListener
{
	public void requestCloneSprite();

	public void requestInsertNewSprite();

	public void requestInsertSpriteFromClipboard();

	public void requestSetSpriteHotspot();

	public void requestEditSprite();

	public void requestDeleteSprites();

	public void requestCopySpriteImage();

	public void requestCopySpriteFilename();

	public void requestMakeTransparent();

	public void requestReloadSprites();
}
