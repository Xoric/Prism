package xoric.prism.world.client.map2;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.Point;
import xoric.prism.world.map.AllGrounds;
import xoric.prism.world.map2.GroundType2;
import xoric.prism.world.map2.Map2;

/**
 * @author XoricLee
 * @since 23.02.2014, 12:59:59
 */
public class ClientMap2 extends Map2
{
	private DrawableGround2[][] grounds;

	public ClientMap2()
	{
	}

	// Map:
	@Override
	public void setSize(int tilesX, int tilesY)
	{
		super.setSize(tilesX, tilesY);

		int ty = grounds == null ? 0 : grounds.length;
		int tx = grounds == null ? 0 : grounds[0].length;

		DrawableGround2[][] newGrounds = new DrawableGround2[tilesY][tilesX];

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
		{
			int i = h.nextInt();
			GroundType2 g0 = AllGrounds.getGroundType(i);
			GroundType2 g1 = AllGrounds.getGroundType(i + 1);
			grounds[y][x] = new DrawableGround2(g0, g1, new Point(x, y), 0.0f);
		}
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
