package xoric.prism.world.map2;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaList_out;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IMetaChild;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

/**
 * @author XoricLee
 * @since 23.02.2014, 12:59:14
 */
public abstract class Map2 implements IMetaChild
{
	protected final RoutingLayer2 routingLayer;
	private final Point tileCount;

	public Map2()
	{
		routingLayer = new RoutingLayer2();
		tileCount = new Point();
	}

	public void setSize(int tilesX, int tilesY)
	{
		tileCount.x = tilesX;
		tileCount.y = tilesY;
	}

	protected abstract void loadGrounds(int y, Heap_in h) throws PrismException;

	protected abstract void saveGrounds(int y, Heap_out h);

	protected abstract void loadWeather(int y, Heap_in h) throws PrismException;

	protected abstract void saveWeather(int y, Heap_out h);

	public IPoint_r getTileCount()
	{
		return tileCount;
	}

	// IMetaChild_out:
	@Override
	public void save(MetaList_out metaList) throws PrismException
	{
		MetaBlock_out mb = new MetaBlock_out(MetaType.MAP, 0);
		metaList.addMetaBlock(mb);

		// size
		MetaLine_out sizeLine = new MetaLine_out(MetaKey.SIZE);
		tileCount.appendTo(sizeLine.getHeap());
		mb.addMetaLine(sizeLine);

		// grounds and weather
		for (int iy = 0; iy < tileCount.y; ++iy)
		{
			// ground
			MetaLine_out ml = new MetaLine_out(MetaKey.ITEM);
			saveGrounds(iy, ml.getHeap());
			mb.addMetaLine(ml);

			// weather
			ml = new MetaLine_out(MetaKey.SUB);
			saveWeather(iy, ml.getHeap());
			mb.addMetaLine(ml);
		}
	}

	// IMetaChild_in:
	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.MAP);

		// size
		MetaLine_in sizeLine = mb.claimLine(MetaKey.SIZE);
		tileCount.extractFrom(sizeLine.getHeap());
		setSize(tileCount.x, tileCount.y);

		// grounds and weather
		int iyGround = 0;
		int iyWeather = 0;

		for (int i = 0; i < mb.getLineCount(); ++i)
		{
			MetaLine_in ml = mb.getMetaLine(i);

			if (ml.getKey() == MetaKey.ITEM)
			{
				// ground
				if (iyGround >= tileCount.y)
					onInvalidDimension(ml, "rows", tileCount.y, iyGround, "grounds");
				if (ml.getHeap().ints.size() != tileCount.x)
					onInvalidDimension(ml, "columns", tileCount.x, ml.getHeap().ints.size(), "grounds");

				loadGrounds(iyGround, ml.getHeap());
				++iyGround;
			}
			else if (ml.getKey() == MetaKey.SUB)
			{
				// weather
				if (iyWeather >= tileCount.y)
					onInvalidDimension(ml, "rows", tileCount.y, iyWeather, "weather");
				if (ml.getHeap().ints.size() != tileCount.x)
					onInvalidDimension(ml, "columns", tileCount.x, ml.getHeap().ints.size(), "weather");

				loadWeather(iyWeather, ml.getHeap());
				++iyWeather;
			}
		}

		// ensure correct number of rows
		if (iyGround < tileCount.y)
			onInvalidDimension(mb, "rows", tileCount.y, iyGround, "ground");
		if (iyWeather < tileCount.y)
			onInvalidDimension(mb, "rows", tileCount.y, iyWeather, "weather");
	}

	private void onInvalidDimension(IInfoLayer info, String dimension, int expected, int found, String name) throws PrismException
	{
		boolean tooMany = found > expected;

		PrismException e = new PrismException();
		// ----
		e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
		// ----
		if (tooMany)
			e.code.setText("too many " + dimension + " found while loading " + name, expected, found);
		else
			e.code.setText("not enough " + dimension + " found while loading " + name, expected, found);
		// ----
		info.addExceptionInfoTo(e);
		// ----
		throw e;
	}
}
