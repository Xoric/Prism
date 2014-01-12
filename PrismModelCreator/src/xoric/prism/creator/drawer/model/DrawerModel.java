package xoric.prism.creator.drawer.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.creator.drawer.view.NewModelData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.IntPacker;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.data.types.TextPacker;
import xoric.prism.world.entities.AnimationIndex;

public class DrawerModel implements IPackable
{
	private static final int CURRENT_VERSION = 1;
	private static final String MAIN_FILENAME = "m.meta";

	private Text name;
	private Path path;
	private Point spriteSize;
	private boolean hasChanges;
	private AnimationModel[] animations;

	public DrawerModel()
	{
		name = new Text("");
		path = new Path("");
		spriteSize = new Point();
		hasChanges = false;
		animations = new AnimationModel[AnimationIndex.values().length];
		init();
	}

	public DrawerModel(NewModelData data)
	{
		name = data.getName();
		path = data.getPath();
		spriteSize = data.getTileSize();
		hasChanges = false;
		animations = new AnimationModel[AnimationIndex.values().length];
		init();
	}

	public void init()
	{
		for (int i = 0; i < animations.length; ++i)
			animations[i] = new AnimationModel(path, AnimationIndex.valueOf(i));
	}

	public AnimationModel getAnimation(AnimationIndex a)
	{
		return animations[a.ordinal()];
	}

	public AnimationModel[] getAnimations()
	{
		return animations;
	}

	public IPath_r getPath()
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

	public void setSpriteSize(IPoint_r spriteSize)
	{
		this.spriteSize.x = spriteSize.getX();
		this.spriteSize.y = spriteSize.getY();
	}

	public IPoint_r getSpriteSize()
	{
		return spriteSize;
	}

	public void load(Path path) throws IOException
	{
		this.path = path;

		File file = path.getFile("m.meta");
		FileInputStream in = new FileInputStream(file);
		unpack(in);
		in.close();

		for (AnimationIndex a : AnimationIndex.values())
		{
			AnimationModel m = new AnimationModel(path, a);
			m.load();
			animations[a.ordinal()] = m;
		}
		this.hasChanges = false;
	}

	public void save() throws PrismException
	{
		File file = path.getFile(MAIN_FILENAME);
		try
		{
			FileOutputStream out = new FileOutputStream(file);
			pack(out);
			out.close();

			this.hasChanges = false;
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.WRITE_ERROR);
			// ----
			e.code.setText("error while saving DrawerModel");
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

	@Override
	public int getPackedSize()
	{
		int size = TextPacker.getPackedSize_s(name);
		size += spriteSize.getPackedSize();
		return size;
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
			File file = path.getFile(MAIN_FILENAME);

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
		File mainFile = path.getFile(MAIN_FILENAME);
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