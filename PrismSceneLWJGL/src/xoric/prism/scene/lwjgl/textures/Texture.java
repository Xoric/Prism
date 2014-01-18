package xoric.prism.scene.lwjgl.textures;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.scene.textures.ITexture;

public class Texture implements ITexture
{
	private final int programID;
	private final Point size;

	public Texture(int programID, int width, int height)
	{
		this.programID = programID;
		this.size = new Point(width, height);
	}

	public IPoint_r getSize()
	{
		return size;
	}

	@Override
	public int getTextureID()
	{
		return programID;
	}
}
