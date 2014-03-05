package xoric.prism.scene.art.hotspots;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XoricLee
 * @since 03.03.2014, 23:14:55
 */
public class MarkerCategory implements IMarkerCategory_r
{
	public final List<Marker> markers = new ArrayList<Marker>();

	@Override
	public int getMarkerCount()
	{
		return markers.size();
	}

	@Override
	public Marker getMarker(int index)
	{
		return markers.get(index);
	}
}
