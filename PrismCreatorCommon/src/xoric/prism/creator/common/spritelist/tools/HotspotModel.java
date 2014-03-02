package xoric.prism.creator.common.spritelist.tools;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.common.spritelist.view.HotspotList;

public abstract class HotspotModel implements IHotspotSupplier
{
	protected final List<HotspotList> hotspotLists;
	protected boolean isHotspotListEnabled;

	public HotspotModel()
	{
		hotspotLists = new ArrayList<HotspotList>();
	}

	public boolean isHotspotListEnabled()
	{
		return isHotspotListEnabled;
	}

	public void setHotspotListEnabled(boolean b)
	{
		isHotspotListEnabled = b;
	}

	protected abstract HotspotList createHotspotList(int index);

	@Override
	public final HotspotList getHotspotList(int index)
	{
		HotspotList h = null;

		if (isHotspotListEnabled)
		{
			if (index < hotspotLists.size())
				h = hotspotLists.get(index);

			if (h == null)
				h = createHotspotList(index);
		}
		return h;
	}

	public void setHotspotList(int index, HotspotList h)
	{
		while (hotspotLists.size() <= index)
			hotspotLists.add(null);

		hotspotLists.set(index, h);
	}
}
