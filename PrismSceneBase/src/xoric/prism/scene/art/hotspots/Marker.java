package xoric.prism.scene.art.hotspots;

import xoric.prism.data.types.IFloatPoint_r;

/**
 * @author XoricLee
 * @since 03.03.2014, 22:54:37
 */
public class Marker
{
	private final IFloatPoint_r hotspot;
	private final ActionPoint[] actionPoints;

	public Marker(IFloatPoint_r hotspot, ActionPoint[] actionPoints)
	{
		this.hotspot = hotspot;
		this.actionPoints = actionPoints;
	}

	public IFloatPoint_r getHotspot()
	{
		return hotspot;
	}

	public int getActionPointCount()
	{
		return actionPoints.length;
	}

	public ActionPoint getActionPoint(int index)
	{
		return actionPoints[index];
	}
}
