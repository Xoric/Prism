package xoric.prism.scene.lwjgl.textures;

import org.lwjgl.opengl.GL11;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.scene.art.ITexture;
import xoric.prism.scene.lwjgl.ICleanUp;

public class Texture implements ITexture, ICleanUp
{
	private int programID;
	private final Point size;

	public Texture(int programID, int width, int height)
	{
		this.programID = programID;
		this.size = new Point(width, height);
	}

	@Override
	public IPoint_r getSize()
	{
		return size;
	}

	@Override
	public int getTextureID()
	{
		return programID;
	}

	@Override
	public String toString()
	{
		return "texture w=" + size.x + ", h=" + size.y;
	}

	// ICleanUp:
	@Override
	public void cleanUp() throws Exception
	{
		if (programID > 0)
		{
			GL11.glDeleteTextures(programID);
			programID = 0;
		}
	}
}
