package xoric.prism.scene.textures.collections;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;

public class ObjectMeta
{
	private final IText_r name;
	private final FloatPoint objectFraction;
	private final FloatPoint sizePixels;
	private final List<ObjectInstance> instances;

	// used while creation:
	private List<FloatRect> tempRects;
	private List<IFloatPoint_r> sizes;

	public ObjectMeta(IText_r name, FloatPoint objectFraction, FloatPoint sizePixels)
	{
		this.name = name;
		this.objectFraction = objectFraction;
		this.sizePixels = sizePixels;
		this.instances = new ArrayList<ObjectInstance>();

		this.tempRects = new ArrayList<FloatRect>();
		this.sizes = new ArrayList<IFloatPoint_r>();
	}

	@Override
	public String toString()
	{
		return name.toString();
	}

	public IFloatPoint_r getSize()
	{
		return sizePixels;
	}

	public IText_r getName()
	{
		return name;
	}

	public ObjectInstance getInstance(int index)
	{
		return instances.get(index);
	}

	public int getInstanceCount()
	{
		return instances.size();
	}

	public void addSubRect(FloatRect rect, IFloatPoint_r size, MetaLine ml) throws PrismException
	{
		if (instances.size() > 0)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("adding rects is forbidden after an object instance was added");
			ml.addExceptionInfoTo(e);
			throw e;
		}
		tempRects.add(rect);
		sizes.add(size);
	}

	public void addPosition(float fx, float fy)
	{
		ObjectInstance o = new ObjectInstance();

		if (tempRects.size() == 0)
		{
			o.addRect(new FloatRect(fx, fy, objectFraction.x, objectFraction.y), sizePixels);
		}
		else
		{
			for (int i = 0; i < tempRects.size(); ++i)
			{
				FloatRect fr = tempRects.get(i);
				o.addRect(new FloatRect(fx + fr.getX(), fy + fr.getY(), fr.getWidth(), fr.getHeight()), sizes.get(i));
			}
		}

		instances.add(o);
	}

	public void finish()
	{
		tempRects = null;
		sizes = null;
	}
}
