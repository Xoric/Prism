package xoric.prism.creator.custom.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.IntPacker;
import xoric.prism.data.types.Text;
import xoric.prism.data.types.TextPacker;

public class SpriteCollectionModel implements IPackable
{
	private static final String metaName = "objects.meta";

	private Text name;
	private final IPath_r path;
	private final List<ObjectModel> objects;

	public SpriteCollectionModel(Text name, IPath_r path)
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
		File f = path.getFile(metaName);

		if (f.exists())
		{
			FileInputStream stream = new FileInputStream(f);
			unpack(stream);
			stream.close();
		}
	}

	public void save() throws IOException
	{
		FileOutputStream stream = new FileOutputStream(path.getFile(metaName));
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
		name = TextPacker.unpack_s(stream);
		int n = IntPacker.unpack_s(stream);

		for (int i = 0; i < n; ++i)
		{
			ObjectModel m = new ObjectModel();
			m.unpack(stream);
			objects.add(m);
		}
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		TextPacker.pack_s(stream, name);
		int n = objects.size();
		IntPacker.pack_s(stream, n);

		for (ObjectModel m : objects)
			m.pack(stream);
	}

	public void deleteObject(int index)
	{
		objects.remove(index);

	}

	public int getObjectCount()
	{
		return objects.size();
	}

	public IText_r getName()
	{
		return name;
	}
}
