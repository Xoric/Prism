package xoric.prism.creator.ground.model;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Point;
import xoric.prism.world.client.map2.DrawableGround2;
import xoric.prism.world.map.AllGrounds;
import xoric.prism.world.map2.GroundType2;

/**
 * @author XoricLee
 * @since 24.02.2014, 10:56:36
 */
public class AllDrawableGrounds
{
	private static List<DrawableGround2> list = new ArrayList<DrawableGround2>();

	public static synchronized void loadAll() throws PrismException
	{
		final int columns = 3;
		int x = 0;
		int y = 0;

		list.clear();
		for (GroundType2 g : AllGrounds.list)
		{
			list.add(new DrawableGround2(g, g, new Point(x, y), 0.0f));
			if (++x >= columns)
			{
				x = 0;
				++y;
			}
		}
	}

	public static synchronized void add(DrawableGround2 g)
	{
		list.add(g);
	}

	public static synchronized DrawableGround2 get(int index)
	{
		return list.get(index);
	}

	public static synchronized int getCount()
	{
		return list.size();
	}
}
