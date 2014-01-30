package xoric.prism.scene.textures.collections;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.Heap;
import xoric.prism.data.types.IMetaChild;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Rect;

public class CollectionMeta implements IMetaChild
{
	private FloatPoint textureSize;
	private List<ObjectMeta> objects;

	public CollectionMeta()
	{
		objects = new ArrayList<ObjectMeta>();
	}

	@Override
	public void load(MetaList metaList) throws PrismException
	{
		objects.clear();
		MetaBlock mb = metaList.claimMetaBlock(MetaType.COLLECTION);
		ObjectMeta om = null;

		for (MetaLine ml : mb.getMetaLines())
		{
			om = interpretLine(om, ml);
		}

		for (ObjectMeta o : objects)
			o.finish();
	}

	public int getObjectCount()
	{
		return objects.size();
	}

	public ObjectMeta getObject(int index)
	{
		return objects.get(index);
	}

	private ObjectMeta interpretLine(ObjectMeta om, MetaLine ml) throws PrismException
	{
		MetaKey key = ml.getKey();

		if (key == MetaKey.SIZE)
		{
			interpretSize(ml);
		}
		else if (key == MetaKey.ITEM)
		{
			om = interpretItem(ml);
		}
		else if (key == MetaKey.ALT)
		{
			interpretAlt(om, ml);
		}
		else if (key == MetaKey.SUB)
		{
			interpretSub(om, ml);
		}
		return om;
	}

	private void interpretSize(MetaLine ml) throws PrismException
	{
		ml.ensureMinima(2, 0, 0);
		Heap h = ml.getHeap();
		int width = h.ints.get(0);
		int height = h.ints.get(1);
		textureSize = new FloatPoint(width, height);
	}

	private void ensureSizeIsSet(MetaLine ml) throws PrismException
	{
		if (textureSize == null)
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			e.code.setText("texture size was null but is required to interpret this line");
			ml.addExceptionInfoTo(e);
			throw e;
		}
	}

	private ObjectMeta interpretItem(MetaLine ml) throws PrismException
	{
		ml.ensureMinima(2, 0, 1);
		ensureSizeIsSet(ml);
		Heap h = ml.getHeap();
		IText_r name = h.texts.get(0);
		int width = h.ints.get(0);
		int height = h.ints.get(1);
		float fw = width / textureSize.x;
		float fh = height / textureSize.y;
		FloatPoint objectFraction = new FloatPoint(fw, fh);
		ObjectMeta om = new ObjectMeta(name, objectFraction);
		objects.add(om);

		return om;
	}

	private void interpretAlt(ObjectMeta om, MetaLine ml) throws PrismException
	{
		ml.ensureMinima(4, 0, 0);
		ensureSizeIsSet(ml);
		Heap h = ml.getHeap();
		Rect rect = new Rect();
		rect.extractFrom(h);
		float fx = rect.getX() / textureSize.x;
		float fy = rect.getY() / textureSize.y;
		float fw = rect.getWidth() / textureSize.x;
		float fh = rect.getHeight() / textureSize.y;
		FloatRect rectFraction = new FloatRect(fx, fy, fw, fh);
		om.addSubRect(rectFraction, ml);
	}

	private void interpretSub(ObjectMeta om, MetaLine ml) throws PrismException
	{
		ml.ensureMinima(2, 0, 0);
		ensureSizeIsSet(ml);
		Heap h = ml.getHeap();
		float fx = h.ints.get(0) / textureSize.x;
		float fy = h.ints.get(1) / textureSize.y;
		om.addPosition(fx, fy);
	}
}
