package xoric.prism.creator.common.spritelist.view;

/**
 * @author XoricLee
 * @since 28.02.2014, 17:02:30
 */
public interface IHotspotListener
{
	public void setHotspotList(int spriteIndex, HotspotList list);

	public HotspotList getHotspotList(int spriteIndex);
}
