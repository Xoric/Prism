package xoric.prism.creator.common.spritelist.tools;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.common.spritelist.view.ActionPoint;
import xoric.prism.creator.common.spritelist.view.HotspotList;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.Point;

public abstract class HotspotWriter
{
	/**
	 * Inserts a point if it does not exist yet. Returns an index.
	 * @param coordinates
	 * @param f
	 * @return int
	 */
	private static int makeIndex(List<Point> coordinates, FloatPoint f)
	{
		Point p = new Point((int) f.x, (int) f.y);

		for (int i = 0; i < coordinates.size(); ++i)
			if (coordinates.get(i).equals(p))
				return i;

		coordinates.add(p);
		return coordinates.size() - 1;
	}

	public static MetaBlock_out createMetaBlock(IHotspotSupplier host) throws PrismException
	{
		MetaBlock_out hotspotBlock = new MetaBlock_out(MetaType.HOTSPOTS, 0);
		List<Point> coordinates = new ArrayList<Point>();

		for (int k = 0; k < host.getCategoryCount(); ++k)
		{
			final int elementCount = host.getElementCount(k);
			MetaLine_out ml = new MetaLine_out(MetaKey.SUB);
			Heap_out heap = ml.getHeap();
			heap.ints.add(elementCount);

			for (int i = 0; i < elementCount; ++i)
			{
				HotspotList h = host.getHotspotList(k, i);
				int hotspotIndex = makeIndex(coordinates, h.hotspot);
				heap.ints.add(hotspotIndex);

				// add number of action points
				final int actionPointCount = h.actionPoints.size();
				heap.ints.add(actionPointCount);

				// add action points
				for (int j = 0; j < actionPointCount; ++j)
				{
					ActionPoint p = h.actionPoints.get(j);
					int actionPointIndex = makeIndex(coordinates, p.down);
					heap.ints.add(actionPointIndex);
					p.angle.appendTo(heap);
				}
			}
			hotspotBlock.addMetaLine(ml);
		}

		// insert coordinates
		MetaLine_out coordinatesLine = new MetaLine_out(MetaKey.ITEM);
		hotspotBlock.insertMetaLine(0, coordinatesLine);
		Heap_out heap = coordinatesLine.getHeap();
		final int n = coordinates.size();
		heap.ints.add(n);
		for (Point p : coordinates)
		{
			heap.ints.add(p.x);
			heap.ints.add(p.y);
		}

		// return MetaBlock
		return hotspotBlock;
	}
}
