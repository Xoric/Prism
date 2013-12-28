package xoric.prism.world.map;

import xoric.prism.data.IPoint_r;
import xoric.prism.data.Point;

public class Map
{
	private Point tileCount;
	private Point size;

	public Map()
	{
		tileCount = new Point();
		size = new Point();
		resize(20, 20);
	}

	public void resize(int tilesX, int tilesY)
	{
		tileCount.x = tilesX;
		tileCount.y = tilesY;
		size.x = tilesX * Tile.WIDTH;
		size.y = tilesY * Tile.HEIGHT;
	}

	public IPoint_r getTileCount()
	{
		return tileCount;
	}

	public IPoint_r getSize()
	{
		return size;
	}
}
