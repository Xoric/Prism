package xoric.prism.world.server;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.Point;
import xoric.prism.world.map.AllGrounds;
import xoric.prism.world.map2.Ground2;
import xoric.prism.world.map2.Map2;

/**
 * @author XoricLee
 * @since 23.02.2014, 23:04:28
 */
public class ServerMap2 extends Map2
{
	private Ground2[][] grounds;

	public ServerMap2()
	{
	}

	// Map:
	@Override
	public void setSize(int tilesX, int tilesY)
	{
		super.setSize(tilesX, tilesY);

		int ty = grounds == null ? 0 : grounds.length;
		int tx = grounds == null ? 0 : grounds[0].length;

		Ground2[][] newGrounds = new Ground2[tilesY][tilesX];

		if (tx > 0 && ty > 0)
			for (int y = 0; y < tilesY; ++y)
				for (int x = 0; x < tilesX; ++x)
					newGrounds[y][x] = y < ty && x < tx ? grounds[y][x] : null;

		grounds = newGrounds;
	}

	// Map:
	@Override
	protected void loadGrounds(int y, Heap_in h) throws PrismException
	{
		for (int x = 0; x < grounds[y].length; ++x)
			grounds[y][x] = new Ground2(AllGrounds.getGroundType(h.nextInt()), new Point(x, y));
	}

	// Map:
	@Override
	protected void saveGrounds(int y, Heap_out h)
	{
		for (int x = 0; x < grounds[y].length; ++x)
			h.ints.add(grounds[y][x].getGroundType().getIndex());
	}

	// Map:
	@Override
	protected void loadWeather(int y, Heap_in h) throws PrismException
	{
		for (int x = 0; x < grounds[y].length; ++x)
			grounds[y][x].getWeatherFader().setWeather(h.nextInt());
	}

	// Map:
	@Override
	protected void saveWeather(int y, Heap_out h)
	{
		for (int x = 0; x < grounds[y].length; ++x)
			h.ints.add(grounds[y][x].getWeatherFader().getWeather());
	}
}
