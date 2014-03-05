package xoric.prism.creator.common.spritelist.view;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.types.FloatPoint;

/**
 * @author XoricLee
 * @since 28.02.2014, 18:22:18
 */
public class HotspotList implements IStackable
{
	public final FloatPoint hotspot;
	public final List<ActionPoint> actionPoints;

	public HotspotList()
	{
		this.hotspot = new FloatPoint();
		this.actionPoints = new ArrayList<ActionPoint>();
	}

	public void setDefaultHotspot(int spriteWidth, int spriteHeight)
	{
		hotspot.x = spriteWidth / 2;
		hotspot.y = spriteHeight - 2;
	}

	@Override
	public String toString()
	{
		return "hotspot: x=" + (int) hotspot.x + ", y=" + (int) hotspot.y + ", action points: " + actionPoints.size();
	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add((int) hotspot.x);
		h.ints.add((int) hotspot.y);

		h.ints.add(actionPoints.size());
		for (ActionPoint p : actionPoints)
			p.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		actionPoints.clear();

		hotspot.x = h.nextInt();
		hotspot.y = h.nextInt();

		final int n = h.nextInt();

		for (int i = 0; i < n; ++i)
		{
			ActionPoint p = new ActionPoint();
			p.extractFrom(h);
			actionPoints.add(p);
		}
	}
}
