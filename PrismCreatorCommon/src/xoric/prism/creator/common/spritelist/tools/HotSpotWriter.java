package xoric.prism.creator.common.spritelist.tools;

import xoric.prism.creator.common.spritelist.view.HotSpotList;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.meta.MetaType;

public abstract class HotSpotWriter
{
	public static MetaBlock_out createMetaBlock(IHotSpotSupplier host, int count)
	{
		MetaBlock_out hotspotBlock = new MetaBlock_out(MetaType.HOTSPOTS, 0);

		for (int i = 0; i < count; ++i)
		{
			HotSpotList hsl = host.getHotSpotList(i);
			MetaLine_out ml = new MetaLine_out(MetaKey.ITEM);
			hsl.appendTo(ml.getHeap());
			hotspotBlock.addMetaLine(ml);
		}
		return hotspotBlock;
	}
}
