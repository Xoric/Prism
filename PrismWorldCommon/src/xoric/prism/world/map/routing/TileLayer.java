package xoric.prism.world.map.routing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.packable.IPackable;
import xoric.prism.data.types.Point;
import xoric.prism.world.map.Ground;
import xoric.prism.world.map.IMapLayer;

public class TileLayer implements IMapLayer, IPackable
{
	// routing tiles
	private RoutingTile[][] tiles;

	/**
	 * Resizes the Map to the given number of tiles in x- and y-direction. Copies any existing tiles.
	 * @param tilesX
	 * @param tilesY
	 */
	@Override
	public void setSize(int tilesX, int tilesY)
	{
		// copy existing tiles
		RoutingTile[][] newTiles = null;
		if (tilesX > 0 && tilesY > 0)
		{
			newTiles = new RoutingTile[tilesY][tilesX];
			if (tiles != null)
			{
				for (int iy = 0; iy < tilesY; ++iy)
				{
					newTiles[iy] = new RoutingTile[tilesX];
					for (int ix = 0; ix < tilesX; ++ix)
						newTiles[iy][ix] = null; // (iy < tiles.length && ix < tiles[iy].length) ? tiles[iy][ix] : new RoutingTile(new Point(ix, iy), AllGrounds.GROUND0); 
					// (disabled)
				}
			}
		}
		tiles = newTiles;
	}

	public RoutingTile[][] getTiles()
	{
		return tiles;
	}

	public RoutingTile getTile(int ix, int iy)
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

	@Deprecated
	public void setTile(int x, int y, int k) // TODO: this method is only temporary
	{
		tiles[y][x] = new RoutingTile(new Point(x, y), new Ground(k));
	}
	//	@Override
	//	public int getPackedSize()
	//	{
	//		int size = 0;
	//
	//		for (int iy = 0; iy < tiles.length; ++iy)
	//			for (int ix = 0; ix < tiles[iy].length; ++ix)
	//				size += tiles[iy][ix].getPackedSize();
	//
	//		return size;
	//	}
}
