package xoric.prism.creator.common.spritelist.tools;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.common.spritelist.view.ActionPoint;
import xoric.prism.creator.common.spritelist.view.HotspotList;
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

	public static MetaBlock_out createMetaBlock(IHotspotSupplier host, int count)
	{
		MetaBlock_out hotspotBlock = new MetaBlock_out(MetaType.HOTSPOTS, 0);
		List<Point> coordinates = new ArrayList<Point>();

		// add a hotspot list for each sprite
		for (int i = 0; i < count; ++i)
		{
			// add hotspot
			MetaLine_out ml = new MetaLine_out(MetaKey.SUB);
			Heap_out heap = ml.getHeap();
			HotspotList h = host.getHotspotList(i);
			int hotspotIndex = makeIndex(coordinates, h.hotSpot);
			heap.ints.add(hotspotIndex);

			// add number of action points
			final int n = h.actionPoints.size();
			heap.ints.add(n);

			// add action points
			for (int j = 0; j < n; ++j)
			{
				ActionPoint p = h.actionPoints.get(j);
				int actionPointIndex = makeIndex(coordinates, p.down);
				heap.ints.add(actionPointIndex);
				p.angle.appendTo(heap);
			}
			hotspotBlock.addMetaLine(ml);
		}

		// insert coordinates
		MetaLine_out coordinateLine = new MetaLine_out(MetaKey.ITEM);
		hotspotBlock.insertMetaLine(0, coordinateLine);
		Heap_out heap = coordinateLine.getHeap();
		final int n = coordinates.size();
		heap.ints.add(n);
		for (Point p : coordinates)
			p.appendTo(heap);

		// return MetaBlock
		return hotspotBlock;
	}
}
