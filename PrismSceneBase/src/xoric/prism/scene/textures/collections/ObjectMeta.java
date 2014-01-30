package xoric.prism.scene.textures.collections;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IText_r;

public class ObjectMeta
{
	private final IText_r name;
	private final FloatPoint objectFraction;
	private final List<ObjectInstance> instances;

	// used while creation:
	private List<FloatRect> tempRects;

	public ObjectMeta(IText_r name, FloatPoint objectFraction)
	{
		this.name = name;
		this.objectFraction = objectFraction;
		this.instances = new ArrayList<ObjectInstance>();
		this.tempRects = new ArrayList<FloatRect>();
	}

	@Override
	public String toString()
	{
		return name.toString();
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

	public void addSubRect(FloatRect rect, MetaLine ml) throws PrismException
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
	}

	public void addPosition(float x, float y)
	{
		ObjectInstance o = new ObjectInstance();

		if (tempRects.size() == 0)
			o.addRect(new FloatRect(x, y, objectFraction.x, objectFraction.y));
		else
			for (FloatRect fr : tempRects)
				o.addRect(new FloatRect(x + fr.topLeft.x, y + fr.topLeft.y, fr.size.x, fr.size.y));

		instances.add(o);
	}

	public void finish()
	{
		tempRects = null;
	}
}
