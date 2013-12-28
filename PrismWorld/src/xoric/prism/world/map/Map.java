package xoric.prism.world.map;

import xoric.prism.data.IPoint_r;
import xoric.prism.data.Point;

public class Map
{
	// size information (tiles and pixels)
	private Point tileCount;
	private Point size;

	// ground tiles
	private Tile[][] tiles;

	public Map()
	{
		tileCount = new Point(0, 0);
		size = new Point(0, 0);
	}

	/**
	 * Resizes the Map to the given number of tiles in x- and y-direction. Copies any existing tiles.
	 * @param tilesX
	 * @param tilesY
	 */
	public void setSize(int tilesX, int tilesY)
	{
		// copy existing tiles
		Tile[][] newTiles = null;
		if (tilesX > 0 && tilesY > 0)
		{
			newTiles = new Tile[tilesY][tilesX];
			if (tiles != null)
			{
				for (int iy = 0; iy < tilesY; ++iy)
				{
					newTiles[iy] = new Tile[tilesX];
					for (int ix = 0; ix < tilesX; ++ix)
						newTiles[iy][ix] = (iy < tiles.length && ix < tiles[iy].length) ? tiles[iy][ix] : new Tile();
				}
			}
		}
		tiles = newTiles;

		// update size information
		tileCount.x = tilesX;
		tileCount.y = tilesY;
		size.x = tilesX * Tile.WIDTH;
		size.y = tilesY * Tile.HEIGHT;
	}

	/**
	 * Returns the number of tiles in x- and y-direction.
	 * @return IPoint_r
	 */
	public IPoint_r getTileCount()
	{
		return tileCount;
	}

	/**
	 * Returns width and height of the Map in pixels.
	 * @return IPoint_r
	 */
	public IPoint_r getSize()
	{
		return size;
	}
}
