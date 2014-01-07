package xoric.prism.creator.drawer.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.creator.drawer.view.NewModelData;
import xoric.prism.data.exceptions2.PrismException2;
import xoric.prism.data.exceptions2.UserErrorText;
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
	private static final String MAIN_FILENAME = "m.meta";

	private Text name;
	private Path path;
	private Point tileSize;
	private boolean hasChanges;

	public DrawerModel()
	{
		name = new Text("");
		path = new Path("");
		tileSize = new Point();
		hasChanges = false;
	}

	public DrawerModel(NewModelData data)
	{
		name = data.getName();
		path = data.getPath();
		tileSize = data.getTileSize();
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

	public void load(Path path) throws IOException
	{
		this.path = path;

		File file = path.getFile("m.meta");
		FileInputStream in = new FileInputStream(file);
		unpack(in);
		in.close();

		this.hasChanges = false;
	}

	public void save() throws PrismException2
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
			PrismException2 e = new PrismException2(e0);
			// ----
			e.user.setText(UserErrorText.WRITE_ERROR);
			// ----
			e.code.setText("error while saving a DrawerModel");
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

	public void preparePath() throws PrismException2
	{
		// try to create path if it does not exist yet
		if (!path.exists())
		{
			boolean wasCreated = path.createDirectories();

			if (!wasCreated)
			{
				PrismException2 e = new PrismException2();
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
				PrismException2 e = new PrismException2();
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
			PrismException2 e = new PrismException2();
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
