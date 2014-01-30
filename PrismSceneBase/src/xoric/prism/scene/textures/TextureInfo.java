package xoric.prism.scene.textures;

import xoric.prism.data.types.IFloatRect_r;

public class TextureInfo
{
	private final int textureID;
	private final IFloatRect_r rect;
	private final boolean flipH;

	public TextureInfo(int textureID, IFloatRect_r rect, boolean flipH)
	{
		this.textureID = textureID;
		this.rect = rect;
		this.flipH = flipH;
	}

	public int getTextureID()
	{
		return textureID;
	}

	public IFloatRect_r getRect()
	{
		return rect;
	}
}
