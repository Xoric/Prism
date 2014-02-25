package xoric.prism.scene.textures.collections;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public class ObjectInstance
{
	private final List<FloatRect> rects;
	private final List<IFloatPoint_r> sizes;

	public ObjectInstance()
	{
		rects = new ArrayList<FloatRect>();
		sizes = new ArrayList<IFloatPoint_r>();
	}

	public void addRect(FloatRect rect, IFloatPoint_r size)
	{
		rects.add(rect);
		sizes.add(size);
	}

	@Override
	public String toString()
	{
		return rects.size() + " rect(s)";
	}

	public IFloatRect_r getRect(int index)
	{
		return rects.get(index);
	}

	public IFloatPoint_r getSize(int index)
	{
		return sizes.get(index);
	}

	public int getRectCount()
	{
		return rects.size();
	}
}
