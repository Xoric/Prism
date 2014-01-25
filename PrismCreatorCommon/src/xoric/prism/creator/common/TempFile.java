package xoric.prism.creator.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPackable;
import xoric.prism.data.types.Path;

public abstract class TempFile implements IPackable
{
	private final File file;

	public TempFile(String filename)
	{
		Path tempPath = new Path(System.getProperty("java.io.tmpdir"));
		file = tempPath.getFile(filename);
	}

	public boolean save()
	{
		boolean b = false;
		try
		{
			FileOutputStream stream = new FileOutputStream(file);
			pack(stream);
			stream.close();
			b = true;
		}
		catch (Exception e)
		{
		}
		return b;
	}

	public boolean load()
	{
		boolean b = false;
		try
		{
			if (file.exists())
			{
				FileInputStream stream = new FileInputStream(file);
				unpack(stream);
				stream.close();
				b = true;
			}
		}
		catch (PrismException e)
		{
		}
		catch (Exception e)
		{
		}
		return b;
	}
}
