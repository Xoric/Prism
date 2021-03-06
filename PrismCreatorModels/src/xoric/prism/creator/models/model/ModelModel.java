package xoric.prism.creator.models.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.creator.models.view.NewModelData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.packable.TextPacker;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.world.animations.AnimationIndex;

public class ModelModel implements IPackable
{
	public static final String mainFilename = "m.meta";
	private static final int CURRENT_VERSION = 1;

	private Text name;
	private Path path;
	private Point spriteSize;
	private VariationList[] list;

	public ModelModel()
	{
		name = new Text("");
		path = new Path("");
		spriteSize = new Point();
		list = new VariationList[AnimationIndex.values().length];
		init();
	}

	public ModelModel(NewModelData data)
	{
		name = data.getName();
		path = data.getPath();
		spriteSize = data.getTileSize();
		list = new VariationList[AnimationIndex.values().length];
		init();
	}

	public void init()
	{
		for (int i = 0; i < list.length; ++i)
			list[i] = new VariationList(path, AnimationIndex.valueOf(i));
	}

	public VariationList getAnimation(AnimationIndex a)
	{
		return list[a.ordinal()];
	}

	public VariationList[] getAnimationList()
	{
		return list;
	}

	public IPath_r getPath()
	{
		return path;
	}

	public IText_r getName()
	{
		return name;
	}

	public void setName(IText_r name)
	{
		this.name.set(name);
	}

	public void setSpriteSize(IPoint_r spriteSize)
	{
		this.spriteSize.x = spriteSize.getX();
		this.spriteSize.y = spriteSize.getY();
	}

	public IPoint_r getSpriteSize()
	{
		return spriteSize;
	}

	public void load(IPath_r path) throws IOException, PrismException
	{
		this.path = new Path(path);

		// afterwards load model meta (updates animations as well)
		File file = path.getFile("m.meta");
		FileInputStream stream = new FileInputStream(file);
		unpack(stream);

		for (AnimationIndex a : AnimationIndex.values())
		{
			VariationList l = new VariationList(path, a);
			l.load();
			l.unpack(stream);
			list[a.ordinal()] = l;
		}
		stream.close();
	}

	public void save() throws PrismException
	{
		File file = path.getFile(mainFilename);
		try
		{
			FileOutputStream stream = new FileOutputStream(file);
			pack(stream);
			for (AnimationIndex a : AnimationIndex.values())
				list[a.ordinal()].pack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.WRITE_ERROR);
			// ----
			e.code.setText("error while saving model");
			// ----
			e.addInfo("file", file.toString());
			// ----
			throw e;
		}
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, CURRENT_VERSION);
		TextPacker.pack_s(stream, name);
		spriteSize.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		int version = IntPacker.unpack_s(stream);
		name = TextPacker.unpack_s(stream);
		spriteSize.unpack(stream);
	}

	public void preparePath() throws PrismException
	{
		// try to create path if it does not exist yet
		if (!path.exists())
		{
			boolean wasCreated = path.createDirectories();

			if (!wasCreated)
			{
				PrismException e = new PrismException();
				// ----
				// ----
				// ----
				e.setText("The model's working directory could not be created.");
				e.addInfo("directory", path.toString());
				// ----
				throw e;
			}
		}
		else
		// check if path already contains a model
		{
			File file = path.getFile(mainFilename);

			if (file.exists())
			{
				PrismException e = new PrismException();
				// ----
				// ----
				// ----
				e.setText("The selected working directory already contains a model.");
				e.addInfo("directory", path.toString());
				// ----
				throw e;
			}
		}

		// create main file
		File mainFile = path.getFile(mainFilename);
		boolean wasCreated;
		try
		{
			wasCreated = mainFile.createNewFile();
		}
		catch (IOException e)
		{
			wasCreated = false;
		}

		if (wasCreated)
		{
			save();
		}
		else
		{
			PrismException e = new PrismException();
			// ----
			// ----
			// ----
			e.setText("Could not write to the specified directory.");
			e.addInfo("directory", path.toString());
			// ----
			throw e;
		}
	}
}
