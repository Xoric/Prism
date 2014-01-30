package xoric.prism.scene.textures.collections;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;

public class ObjectInstance
{
	private final List<FloatRect> rects;

	public ObjectInstance()
	{
		rects = new ArrayList<FloatRect>();
	}

	public void addRect(FloatRect fr)
	{
		rects.add(fr);
	}

	public IFloatRect_r getRect(int index)
	{
		return rects.get(index);
	}

	public int getRectCount()
	{
		return rects.size();
	}
}
