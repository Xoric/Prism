package xoric.prism.creator.drawer.image;

public interface ISpriteMenuListener
{
	public void requestInsertSprite();

	public void requestEditSprite();

	public void requestDeleteSprites();

	public void requestCopySpriteFilename();

	public void requestReloadSprite();
}
