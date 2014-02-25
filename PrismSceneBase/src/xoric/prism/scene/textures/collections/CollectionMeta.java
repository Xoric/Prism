package xoric.prism.scene.textures.collections;

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
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Rect;
import xoric.prism.scene.textures.ArtMeta;

public class CollectionMeta extends ArtMeta
{
	private FloatPoint textureSize;
	private final List<ObjectMeta> objects;

	public CollectionMeta()
	{
		objects = new ArrayList<ObjectMeta>();
	}

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		objects.clear();
		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.COLLECTION);
		ObjectMeta om = null;

		for (MetaLine_in ml : mb.getMetaLines())
			om = interpretLine(om, ml);

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

	private ObjectMeta interpretLine(ObjectMeta om, MetaLine_in ml) throws PrismException
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

	private void interpretSize(MetaLine_in ml) throws PrismException
	{
		ml.ensureMinima(2, 0, 0);
		Heap_in h = ml.getHeap();
		int width = h.ints.get(0);
		int height = h.ints.get(1);
		textureSize = new FloatPoint(width, height);
	}

	private void ensureSizeIsSet(MetaLine_in ml) throws PrismException
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

	private ObjectMeta interpretItem(MetaLine_in ml) throws PrismException
	{
		ml.ensureMinima(2, 0, 1);
		ensureSizeIsSet(ml);
		Heap_in h = ml.getHeap();
		IText_r name = h.texts.get(0);
		int width = h.ints.get(0);
		int height = h.ints.get(1);
		float fw = width / textureSize.x;
		float fh = height / textureSize.y;
		FloatPoint objectFraction = new FloatPoint(fw, fh);
		FloatPoint size = new FloatPoint(width, height);

		//		System.out.println("creating ObjectMeta with objectFraction=" + objectFraction);

		ObjectMeta om = new ObjectMeta(name, objectFraction, size);
		objects.add(om);

		return om;
	}

	private void interpretAlt(ObjectMeta om, MetaLine_in ml) throws PrismException
	{
		ml.ensureMinima(4, 0, 0);
		ensureSizeIsSet(ml);
		Heap_in h = ml.getHeap();
		Rect rect = new Rect();
		rect.extractFrom(h);
		float fx = rect.getX() / textureSize.x;
		float fy = rect.getY() / textureSize.y;
		float fw = rect.getWidth() / textureSize.x;
		float fh = rect.getHeight() / textureSize.y;
		FloatRect rectFraction = new FloatRect(fx, fy, fw, fh);
		FloatPoint size = new FloatPoint(rect.getWidth(), rect.getHeight());
		om.addSubRect(rectFraction, size, ml);
	}

	private void interpretSub(ObjectMeta om, MetaLine_in ml) throws PrismException
	{
		ml.ensureMinima(2, 0, 0);
		ensureSizeIsSet(ml);
		Heap_in h = ml.getHeap();
		float x = h.ints.get(0);
		float y = h.ints.get(1);
		float fx = x / textureSize.x;
		float fy = y / textureSize.y;
		om.addPosition(fx, fy);
	}
}
