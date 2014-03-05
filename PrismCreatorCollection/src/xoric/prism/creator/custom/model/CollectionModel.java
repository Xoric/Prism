package xoric.prism.creator.custom.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.common.spritelist.tools.HotspotModel;
import xoric.prism.creator.common.spritelist.view.HotspotList;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaList_out;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.develop.meta.MetaNames;

public class CollectionModel extends HotspotModel implements IPackable
{
	private Text name;
	private final IPath_r path;
	private final List<ObjectModel> objects;

	public CollectionModel(Text name, IPath_r path)
	{
		this.name = new Text(name);
		this.path = path;
		this.objects = new ArrayList<ObjectModel>();
	}

	public void setName(IText_r name)
	{
		this.name = new Text(name);
	}

	public void load() throws IOException, PrismException
	{
		objects.clear();
		File f = path.getFile(MetaNames.projectCollection);

		if (f.exists())
		{
			FileInputStream stream = new FileInputStream(f);
			unpack(stream);
			stream.close();
		}
	}

	public void save() throws IOException, PrismException
	{
		FileOutputStream stream = new FileOutputStream(path.getFile(MetaNames.projectCollection));
		pack(stream);
		stream.close();
	}

	public void addObjectModel(ObjectModel m)
	{
		for (ObjectModel o : objects)
			if (o.getName().equals(m.getName()))
				return;

		objects.add(m);
	}

	@Override
	public String toString()
	{
		return name.toString();
	}

	public IPath_r getPath()
	{
		return path;
	}

	public int getCount()
	{
		return objects.size();
	}

	public ObjectModel getObjectModel(int index)
	{
		return objects.get(index);
	}

	public List<ObjectModel> getObjectModels()
	{
		return objects;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		MetaList_in ml = new MetaList_in();
		ml.unpack(stream);
		MetaBlock_in block = ml.claimMetaBlock(MetaType.COMMON);
		int version = block.getVersion(); // version
		MetaLine_in l = block.getMetaLine(0);
		l.assumeMetaKey(MetaKey.ITEM);
		Heap_in h = l.getHeap();

		name = h.nextText();
		isHotspotListEnabled = version >= 1 ? h.nextBool() : false;
		final int n = h.nextInt();

		for (int i = 0; i < n; ++i)
		{
			ObjectModel m = new ObjectModel();
			m.extractFrom(h);
			objects.add(m);
		}

		// hotspots
		super.extractHotspots(h);
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		MetaList_out ml = new MetaList_out();
		MetaBlock_out block = new MetaBlock_out(MetaType.COMMON, 1); // version
		ml.addMetaBlock(block);
		MetaLine_out l = new MetaLine_out(MetaKey.ITEM);
		block.addMetaLine(l);
		Heap_out h = l.getHeap();
		final int n = objects.size();

		h.texts.add(name);
		h.bools.add(isHotspotListEnabled);
		h.ints.add(n);

		for (ObjectModel m : objects)
			m.appendTo(l.getHeap());

		// hotspots
		super.appendHotspots(h);

		ml.pack(stream);
	}

	public void deleteObject(int index)
	{
		objects.remove(index);
	}

	@Override
	public int getCategoryCount()
	{
		return objects.size();
	}

	public IText_r getName()
	{
		return name;
	}

	public void moveObjectModel(int index, boolean moveUp)
	{
		if (index >= 0 && index < objects.size())
		{
			if (moveUp && index > 0)
			{
				ObjectModel o = objects.get(index);
				objects.remove(index);
				objects.add(index - 1, o);
			}
			else if (!moveUp && index < objects.size() - 1)
			{
				ObjectModel o = objects.get(index);
				objects.remove(index);
				objects.add(index + 1, o);
			}
		}
	}

	//	@Override
	//	protected HotspotList createHotspotList(int index) throws PrismException
	//	{
	//		ObjectModel o = objects.get(objectSelection.getSelectedObjectIndex());
	//		IText_r name = o.getName();
	//
	//		String filename = name.toString().toLowerCase() + ".var0.png";
	//		File f = path.getFile(filename);
	//
	//		BufferedImage bi;
	//		try
	//		{
	//			bi = ImageIO.read(f);
	//		}
	//		catch (IOException e0)
	//		{
	//			PrismException e = new PrismException(e0);
	//			e.setText("There was a problem loading the size of an image.");
	//			e.addInfo("file", f.toString());
	//			throw e;
	//		}
	//
	//		HotspotList h = new HotspotList();
	//		h.setDefaultHotspot(bi.getWidth(), bi.getHeight());
	//		return h;
	//	}

	//	@Override
	//	public HotspotList getHotspotList(int index) throws PrismException
	//	{
	//		int objectIndex = objectSelection.getSelectedObjectIndex();
	//		return getHotspotList2(objectIndex);
	//	}
	//
	//	public HotspotList getHotspotList2(int objectIndex) throws PrismException
	//	{
	//		HotspotList h = null;
	//
	//		if (isHotspotListEnabled)
	//		{
	//			if (objectIndex < categories.size())
	//				h = categories.get(objectIndex);
	//
	//			if (h == null)
	//				h = createHotspotList(objectIndex);
	//		}
	//		return h;
	//	}
	//
	//	@Override
	//	protected void appendHotspots(Heap_out h, int count) throws PrismException
	//	{
	//		h.bools.add(isHotspotListEnabled);
	//
	//		if (isHotspotListEnabled)
	//		{
	//			h.ints.add(count);
	//
	//			for (int i = 0; i < count; ++i)
	//				getHotspotList2(i).appendTo(h);
	//		}
	//	}

	@Override
	public int getElementCount(int categoryIndex)
	{
		ObjectModel o = objects.get(categoryIndex);
		SpriteNameGenerator s = new SpriteNameGenerator(path, o.getName().toString().toLowerCase() + ".var", ".png");
		final int n = s.countSprites();
		return n;
	}

	private static Point importSpriteSize(File f) throws PrismException
	{
		BufferedImage bi;
		try
		{
			bi = ImageIO.read(f);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem loading the size of a sprite.");
			e.addInfo("file", f.toString());
			throw e;
		}
		return new Point(bi.getWidth(), bi.getHeight());
	}

	@Override
	protected HotspotList createHotspotList(int categoryIndex, int elementIndex) throws PrismException
	{
		ObjectModel o = objects.get(categoryIndex);
		SpriteNameGenerator s = new SpriteNameGenerator(path, o.getName().toString().toLowerCase() + ".var", ".png");
		File f = s.getFile(elementIndex);
		Point p = importSpriteSize(f);

		HotspotList h = new HotspotList();
		h.setDefaultHotspot(p.x, p.y);

		return h;
	}
}
