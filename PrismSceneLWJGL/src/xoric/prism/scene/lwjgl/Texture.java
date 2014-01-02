package xoric.prism.scene.lwjgl;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

public class Texture
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
}
