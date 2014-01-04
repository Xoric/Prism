package xoric.prism.creator.drawer.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.IntPacker;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.data.types.TextPacker;

public class DrawerModel implements IPackable
{
	private static final int CURRENT_VERSION = 1;

	private Text name;
	private Point tileSize;
	private Path path;
	private boolean hasChanges;

	public DrawerModel()
	{
		name = new Text("NEW MODEL");
		tileSize = new Point(0, 0);
		path = new Path("");
		hasChanges = false;
	}

	public Path getPath()
	{
		return path;
	}

	public boolean hasChanges()
	{
		return hasChanges;
	}

	public IText_r getName()
	{
		return name;
	}

	public void setName(IText_r name)
	{
		this.hasChanges = true;
		this.name.set(name);
	}

	public void setTileSize(IPoint_r tileSize)
	{
		this.tileSize.x = tileSize.getX();
		this.tileSize.y = tileSize.getY();
	}

	public IPoint_r getTileSize()
	{
		return tileSize;
	}

	public void initPath(Path path) throws IOException
	{
		this.path = path;
		save();
	}

	public void load(Path path) throws IOException
	{
		this.path = path;

		File file = path.getFile("m.meta");
		FileInputStream in = new FileInputStream(file);
		unpack(in);
		in.close();

		this.hasChanges = false;
	}

	public void save() throws IOException
	{
		File file = path.getFile("m.meta");
		FileOutputStream out = new FileOutputStream(file);
		pack(out);
		out.close();

		this.hasChanges = false;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, CURRENT_VERSION);
		TextPacker.pack_s(stream, name);
		tileSize.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		int version = IntPacker.unpack_s(stream);
		name = TextPacker.unpack_s(stream);
		tileSize.unpack(stream);
	}

	@Override
	public int getPackedSize()
	{
		int size = TextPacker.getPackedSize_s(name);
		size += tileSize.getPackedSize();
		return size;
	}

	//	public void createImage(int width, int height)
	//	{
	//		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	//	}
}
