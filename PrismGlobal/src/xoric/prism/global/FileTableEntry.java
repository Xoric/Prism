package xoric.prism.global;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.types.IPath_r;

class FileTableEntry
{
	private IPath_r path;
	private String filename;
	private MetaFile metaFile;

	public FileTableEntry(IPath_r path, String filename)
	{
		this.path = path;
		this.filename = filename;
	}

	@Override
	public String toString()
	{
		return filename;
	}

	public MetaFile loadMetaFile() throws PrismException
	{
		if (metaFile == null)
		{
			if (filename.equals("ui/frames.cl"))
			{
				System.out.println("!");
			}

			metaFile = new MetaFile(path, filename);
			metaFile.load();
		}
		return metaFile;
	}
}
