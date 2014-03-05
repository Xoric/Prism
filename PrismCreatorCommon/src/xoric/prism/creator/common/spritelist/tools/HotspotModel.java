package xoric.prism.creator.common.spritelist.tools;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.common.spritelist.view.HotspotList;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;

public abstract class HotspotModel implements IHotspotSupplier
{
	protected final List<HotspotCategory> categories;
	protected boolean isHotspotListEnabled;

	public HotspotModel()
	{
		categories = new ArrayList<HotspotCategory>();
	}

	public boolean isHotspotListEnabled()
	{
		return isHotspotListEnabled;
	}

	public void setHotspotListEnabled(boolean b)
	{
		isHotspotListEnabled = b;
	}

	protected abstract HotspotList createHotspotList(int categoryIndex, int elementIndex) throws PrismException;

	@Override
	public HotspotList getHotspotList(int categoryIndex, int elementIndex) throws PrismException
	{
		HotspotList h = null;

		if (isHotspotListEnabled)
		{
			for (int i = categories.size(); i <= categoryIndex; ++i)
				categories.add(new HotspotCategory());

			HotspotCategory o = categories.get(categoryIndex);
			h = o.get(elementIndex);

			if (h == null)
				h = createHotspotList(categoryIndex, elementIndex);
		}
		return h;
	}

	public void setHotspotList(int categoryIndex, int elementIndex, HotspotList h)
	{
		for (int i = categories.size(); i <= categoryIndex; ++i)
			categories.add(new HotspotCategory());

		categories.get(categoryIndex).set(elementIndex, h);
	}

	protected void appendHotspots(Heap_out h) throws PrismException
	{
		h.bools.add(isHotspotListEnabled);

		if (isHotspotListEnabled)
		{
			final int categoryCount = this.getCategoryCount();
			h.ints.add(categoryCount);

			for (int c = 0; c < categoryCount; ++c)
			{
				final int elementCount = this.getElementCount(c);
				h.ints.add(elementCount);

				for (int e = 0; e < elementCount; ++e)
				{
					HotspotList hl = getHotspotList(c, e);
					hl.appendTo(h);
				}
			}
		}
	}

	protected void extractHotspots(Heap_in h) throws PrismException
	{
		categories.clear();

		isHotspotListEnabled = h.nextBool();

		if (isHotspotListEnabled)
		{
			final int categoryCount = h.nextInt();

			for (int c = 0; c < categoryCount; ++c)
			{
				HotspotCategory category = new HotspotCategory();
				final int elementCount = h.nextInt();

				for (int e = 0; e < elementCount; ++e)
				{
					HotspotList hl = new HotspotList();
					hl.extractFrom(h);
					category.hotspots.add(hl);
				}
				categories.add(category);
			}
		}
	}
}
