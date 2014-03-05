package xoric.prism.scene.art;

import xoric.prism.data.types.IFloatRect_r;

public class TextureInfo
{
	private final ITexture texture;
	private final IFloatRect_r rect;
	private boolean flipH;

	public TextureInfo(ITexture texture, IFloatRect_r rect, boolean flipH)
	{
		this.texture = texture;
		this.rect = rect;
		this.flipH = flipH;
	}

	@Override
	public String toString()
	{
		return "textureID=" + texture.getTextureID() + ", rect=" + rect + ", flipH=" + flipH;
	}

	public ITexture getTexture()
	{
		return texture;
	}

	public IFloatRect_r getRect()
	{
		return rect;
	}

	public boolean isFlippedH()
	{
		return flipH;
	}

	public void flipH()
	{
		flipH = !flipH;
	}
}
