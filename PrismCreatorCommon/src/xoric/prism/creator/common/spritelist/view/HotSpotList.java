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
public class HotSpotList implements IStackable
{
	public final FloatPoint hotSpot;
	public final List<ActionPoint> actionPoints;

	public HotSpotList()
	{
		this.hotSpot = new FloatPoint();
		this.actionPoints = new ArrayList<ActionPoint>();
	}

	// IStackable_out:
	@Override
	public void appendTo(Heap_out h)
	{
		// hotspot
		h.ints.add((int) hotSpot.x);
		h.ints.add((int) hotSpot.y);

		// action points
		h.ints.add(actionPoints.size());
		for (ActionPoint p : actionPoints)
			p.appendTo(h);
	}

	// IStackable_in:
	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		// hotspot
		hotSpot.x = h.nextInt();
		hotSpot.y = h.nextInt();

		// action points
		int n = h.nextInt();
		for (int i = 0; i < n; ++i)
		{
			ActionPoint p = new ActionPoint();
			p.extractFrom(h);
			actionPoints.add(p);
		}
	}
}
