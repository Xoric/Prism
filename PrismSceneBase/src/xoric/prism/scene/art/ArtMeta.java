package xoric.prism.scene.art;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IMetaChild_in;
import xoric.prism.scene.art.hotspots.HotspotMeta;

public abstract class ArtMeta implements IMetaChild_in
{
	private HotspotMeta hotspotMeta;

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		hotspotMeta = null;

		if (metaList.hasMetaBlock(MetaType.HOTSPOTS))
		{
			hotspotMeta = new HotspotMeta();
			hotspotMeta.load(metaList);
		}
	}

	public HotspotMeta getHotspotMeta()
	{
		return hotspotMeta;
	}
}
