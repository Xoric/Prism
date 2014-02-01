package xoric.prism.creator.grid.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.grid.view.NewGridData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.data.types.TextPacker;

public class GridModel implements IPackable
{
	private static final String metaName = "grid.meta";

	private Text name;
	private Point spriteSize;
	private IPath_r path;
	private SpriteNameGenerator spriteNameGenerator;

	public GridModel(IPath_r path)
	{
		this.path = path;
		this.spriteNameGenerator = new SpriteNameGenerator(path, "sprite", ".png");
	}

	public GridModel(NewGridData d)
	{
		name = d.getName();
		spriteSize = new Point(d.getSpriteSize());
		path = d.getPath();
		spriteNameGenerator = new SpriteNameGenerator(path, "sprite", ".png");
	}

	public SpriteNameGenerator getSpriteNameGenerator()
	{
		return spriteNameGenerator;
	}

	public IText_r getName()
	{
		return name;
	}

	public IPoint_r getSpriteSize()
	{
		return spriteSize;
	}

	public IPath_r getPath()
	{
		return path;
	}

	public void load() throws IOException, PrismException
	{
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

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		name = TextPacker.unpack_s(stream);
		spriteSize = new Point();
		spriteSize.unpack(stream);
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		TextPacker.pack_s(stream, name);
		spriteSize.pack(stream);
	}

	public void setName(IText_r name)
	{
		this.name = new Text(name);
	}

	public void setSpriteSize(IPoint_r spriteSize)
	{
		this.spriteSize = new Point(spriteSize);
	}
}
