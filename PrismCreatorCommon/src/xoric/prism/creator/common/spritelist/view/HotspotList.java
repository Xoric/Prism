package xoric.prism.creator.common.spritelist.view;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.FloatPoint;

/**
 * @author XoricLee
 * @since 28.02.2014, 18:22:18
 */
public class HotspotList
{
	public final FloatPoint hotSpot;
	public final List<ActionPoint> actionPoints;

	public HotspotList()
	{
		this.hotSpot = new FloatPoint();
		this.actionPoints = new ArrayList<ActionPoint>();
	}
}
