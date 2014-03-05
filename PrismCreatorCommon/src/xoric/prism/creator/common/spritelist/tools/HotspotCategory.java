package xoric.prism.creator.common.spritelist.tools;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.common.spritelist.view.HotspotList;
import xoric.prism.data.exceptions.PrismException;

/**
 * @author XoricLee
 * @since 03.03.2014, 17:59:23
 */
public class HotspotCategory
{
	public final List<HotspotList> hotspots;

	public HotspotCategory()
	{
		this.hotspots = new ArrayList<HotspotList>();
	}

	public HotspotList get(int elementIndex) throws PrismException
	{
		return elementIndex < hotspots.size() ? hotspots.get(elementIndex) : null;
	}

	public void set(int spriteIndex, HotspotList h)
	{
		for (int i = hotspots.size(); i <= spriteIndex; ++i)
			hotspots.add(new HotspotList());

		hotspots.set(spriteIndex, h);
	}
}
