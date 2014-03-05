package xoric.prism.creator.common.spritelist.view;

import xoric.prism.data.exceptions.PrismException;

/**
 * @author XoricLee
 * @since 28.02.2014, 17:02:30
 */
public interface IHotspotListener
{
	public void setHotspotList(int spriteIndex, HotspotList list);

	public HotspotList getHotspotList(int spriteIndex) throws PrismException;
}
