package xoric.prism.scene.art.hotspots;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.physics.Angle;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IMetaChild_in;

/**
 * @author XoricLee
 * @since 03.03.2014, 21:46:40
 */
public class HotspotMeta implements IMetaChild_in
{
	private final List<FloatPoint> coordinates;
	private final List<MarkerCategory> categories;

	public HotspotMeta()
	{
		coordinates = new ArrayList<FloatPoint>();
		categories = new ArrayList<MarkerCategory>();
	}

	public int getCategoryCount()
	{
		return categories.size();
	}

	public IMarkerCategory_r getCategory(int index)
	{
		return categories.get(index);
	}

	public Marker getMarker(int category, int element)
	{
		return categories.get(category).markers.get(element);
	}

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		coordinates.clear();
		categories.clear();

		if (metaList.hasMetaBlock(MetaType.HOTSPOTS))
		{
			MetaBlock_in mb = metaList.claimMetaBlock(MetaType.HOTSPOTS);

			// load coordinates
			int index = mb.findNextIndex(MetaKey.ITEM, 0);
			while (index >= 0)
			{
				MetaLine_in ml = mb.getMetaLine(index);
				loadCoordinates(ml.getHeap());
				index = mb.findNextIndex(MetaKey.ITEM, index + 1);
			}

			// load coordinates
			index = mb.findNextIndex(MetaKey.SUB, 0);
			while (index >= 0)
			{
				MetaLine_in ml = mb.getMetaLine(index);
				MarkerCategory c = loadCategory(ml);
				categories.add(c);
				index = mb.findNextIndex(MetaKey.SUB, index + 1);
			}
		}
	}

	private IFloatPoint_r getCoordinates(int index, String name)
	{
		final int n = coordinates.size();

		if (index < 0 || index >= n)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("invalid coordinate index requested for " + name);
			e.code.addInfo("index", index);
			e.code.addInfo("coordinates", n);
		}
		return coordinates.get(index);
	}

	private void loadCoordinates(Heap_in h)
	{
		final int n = h.nextInt();

		for (int i = 0; i < n; ++i)
		{
			int x = h.nextInt();
			int y = h.nextInt();
			FloatPoint p = new FloatPoint(x, y);
			coordinates.add(p);
		}
	}

	private MarkerCategory loadCategory(MetaLine_in ml) throws PrismException
	{
		MarkerCategory category = new MarkerCategory();

		Heap_in h = ml.getHeap();
		final int elementCount = h.nextInt();

		for (int i = 0; i < elementCount; ++i)
		{
			int hotspotIndex = h.nextInt();
			IFloatPoint_r hotspot = getCoordinates(hotspotIndex, "hotspot");

			final int actionPointCount = h.nextInt();
			ActionPoint[] actionPoints = actionPointCount > 0 ? new ActionPoint[actionPointCount] : null;

			for (int j = 0; j < actionPointCount; ++j)
			{
				int actionPointIndex = h.nextInt();
				IFloatPoint_r p = getCoordinates(actionPointIndex, "action point");
				Angle a = new Angle();
				a.extractFrom(h);

				actionPoints[j] = new ActionPoint(p, a);
			}
			Marker m = new Marker(hotspot, actionPoints);
			category.markers.add(m);
		}
		return category;
	}
}
