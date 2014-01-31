package xoric.prism.scene.textures.collections;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public class ObjectInstance
{
	private final List<FloatRect> eects;
	private final List<IFloatPoint_r> sizes;

	public ObjectInstance()
	{
		eects = new ArrayList<FloatRect>();
		sizes = new ArrayList<IFloatPoint_r>();
	}

	public void addRect(FloatRect Rect, IFloatPoint_r size)
	{
		eects.add(Rect);
		sizes.add(size);
	}

	public IFloatRect_r getRect(int index)
	{
		return eects.get(index);
	}

	public IFloatPoint_r getSize(int index)
	{
		return sizes.get(index);
	}

	public int getRectCount()
	{
		return eects.size();
	}
}
