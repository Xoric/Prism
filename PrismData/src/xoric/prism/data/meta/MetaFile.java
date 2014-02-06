package xoric.prism.data.meta;

import java.io.File;
import java.io.FileInputStream;

import xoric.prism.data.exceptions.IInfoLayer;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.types.IPath_r;

public class MetaFile implements IInfoLayer
{
	private final File file;
	private final IPath_r path;
	private final String filename;
	private int localFileVersion;
	private MetaTimeStamp timeStamp;
	private MetaList metaList;
	private AttachmentLoader attachmentLoader;

	public MetaFile(IPath_r path, String filename)
	{
		this.filename = filename;
		this.path = path;
		this.file = path.getFile(filename);
		this.localFileVersion = 0;
		this.timeStamp = new MetaTimeStamp();
		this.metaList = new MetaList();
		this.metaList.setUplink(this);
	}

	public void setMetaList(MetaList metaList)
	{
		this.metaList = metaList;
		this.metaList.setUplink(this);
	}

	public void load() throws PrismException
	{
		if (!file.exists())
		{
			PrismException e = new PrismException();
			// ----
			e.setText(UserErrorText.LOCAL_GAME_FILE_MISSING);
			addExceptionInfoTo(e);
			// ----
			// ----
			throw e;
		}
		try
		{
			// open file as stream
			FileInputStream stream = new FileInputStream(file);

			// unpack version and TimeStamp
			localFileVersion = IntPacker.unpack_s(stream); // 1)
			timeStamp = new MetaTimeStamp();
			timeStamp.unpack(stream); // 2)

			// MetaList and number of attachments
			metaList.unpack(stream); // 3)
			int attachmentCount = IntPacker.unpack_s(stream); // 4)

			// create and unpack AttachmentLoader
			attachmentLoader = new AttachmentLoader(path, filename, attachmentCount);
			attachmentLoader.unpack(stream);

			// close stream
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("an error occured while loading a MetaFile");
			// ----
			addExceptionInfoTo(e);
			// ----
			throw e;
		}
	}

	public void setLocalFileVersion(int version)
	{
		this.localFileVersion = version;
	}

	public MetaTimeStamp getTimeStamp()
	{
		return timeStamp;
	}

	@Override
	public String toString()
	{
		return filename;
	}

	public AttachmentLoader getAttachmentLoader()
	{
		return attachmentLoader;
	}

	public MetaList getMetaList()
	{
		return metaList;
	}

	public int getLocalFileVersion()
	{
		return localFileVersion;
	}

	@Override
	public void setUplink(IInfoLayer uplink)
	{
	}

	@Override
	public void addExceptionInfoTo(PrismException e)
	{
		e.addInfo("file", file.toString());
	}
}
