package xoric.prism.world.map;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

public class Map implements IPackable
{
	// size information (tiles and pixels)
	private Point tileCount;
	private Point size;

	// layers
	private TileLayer tileLayer;
	private RoutingLayer routingLayer;

	public Map()
	{
		// size information
		tileCount = new Point(0, 0);
		size = new Point(0, 0);

		// layers
		tileLayer = new TileLayer();
		routingLayer = null;
	}

	public void setSize(int tilesX, int tilesY)
	{
		// update size information
		tileCount.x = tilesX;
		tileCount.y = tilesY;
		size.x = tilesX * Tile.WIDTH;
		size.y = tilesY * Tile.HEIGHT;

		// update TileLayer
		tileLayer.setSize(tilesX, tilesY);

		// create RoutingLayer
		routingLayer = new RoutingLayer();
		routingLayer.init(tileLayer.getTiles());
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

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// pack tile count
		tileCount.pack(stream);

		// pack TileLayer
		tileLayer.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		// unpack tile count and calculate size
		tileCount.unpack(stream);
		setSize(tileCount.x, tileCount.y);

		// unpack TileLayer
		tileLayer.unpack(stream);

		// create RoutingLayer
		routingLayer = new RoutingLayer();
		routingLayer.init(tileLayer.getTiles());
	}

	@Override
	public int getPackedSize()
	{
		// tile count
		int size = tileCount.getPackedSize();

		// TileLayer
		size += tileLayer.getPackedSize();

		return size;
	}
}
