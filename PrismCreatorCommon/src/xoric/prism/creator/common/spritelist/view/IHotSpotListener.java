package xoric.prism.creator.common.spritelist.view;

/**
 * @author XoricLee
 * @since 28.02.2014, 17:02:30
 */
public interface IHotSpotListener
{
	public void setHotSpot(int spriteIndex, HotSpotList list);

	public HotSpotList getHotSpotList(int spriteIndex);
}
