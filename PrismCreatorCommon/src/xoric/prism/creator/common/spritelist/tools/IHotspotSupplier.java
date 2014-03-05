package xoric.prism.creator.common.spritelist.tools;

import xoric.prism.creator.common.spritelist.view.HotspotList;
import xoric.prism.data.exceptions.PrismException;

public interface IHotspotSupplier
{
	public HotspotList getHotspotList(int categoryIndex, int elementIndex) throws PrismException;

	public int getCategoryCount();

	public int getElementCount(int categoryIndex);
}
