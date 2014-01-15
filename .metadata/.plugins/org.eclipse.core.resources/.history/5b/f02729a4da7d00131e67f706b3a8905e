package xoric.prism.world.map;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.Point;

public class TileLayer implements IMapLayer, IPackable
{
	// ground tiles
	private Tile[][] tiles;

	/**
	 * Resizes the Map to the given number of tiles in x- and y-direction. Copies any existing tiles.
	 * @param tilesX
	 * @param tilesY
	 */
	@Override
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
						newTiles[iy][ix] = (iy < tiles.length && ix < tiles[iy].length) ? tiles[iy][ix] : new Tile(new Point(ix, iy),
								AllGrounds.GROUND0);
				}
			}
		}
		tiles = newTiles;
	}

	public Tile[][] getTiles()
	{
		return tiles;
	}

	public Tile getTile(int ix, int iy)
	{
		return tiles[iy][ix];
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		for (int iy = 0; iy < tiles.length; ++iy)
			for (int ix = 0; ix < tiles[iy].length; ++ix)
				tiles[iy][ix].pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		for (int iy = 0; iy < tiles.length; ++iy)
			for (int ix = 0; ix < tiles[iy].length; ++ix)
				tiles[iy][ix].unpack(stream);
	}

	@Override
	public int getPackedSize()
	{
		int size = 0;

		for (int iy = 0; iy < tiles.length; ++iy)
			for (int ix = 0; ix < tiles[iy].length; ++ix)
				size += tiles[iy][ix].getPackedSize();

		return size;
	}
}
