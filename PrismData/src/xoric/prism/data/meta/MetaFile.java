package xoric.prism.data.meta;

import java.io.File;
import java.io.FileInputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IntPacker;

public class MetaFile implements IMetaListOwner
{
	private final File file;
	private final IPath_r path;
	private final String filename;
	private int localFileVersion;
	private TimeStamp timeStamp;
	private MetaList metaList;
	private AttachmentLoader attachmentLoader;

	public MetaFile(IPath_r path, String filename)
	{
		this.filename = filename;
		this.path = path;
		this.file = path.getFile(filename);
		this.localFileVersion = 0;
		this.timeStamp = new TimeStamp();
		this.metaList = new MetaList();
		this.metaList.setOwner(this);
	}

	public void setMetaList(MetaList metaList)
	{
		this.metaList = metaList;
		this.metaList.setOwner(this);
	}

	public void load() throws PrismException
	{
		try
		{
			// open file as stream
			FileInputStream stream = new FileInputStream(file);

			// unpack version and TimeStamp
			localFileVersion = IntPacker.unpack_s(stream); // 1)
			timeStamp = new TimeStamp();
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
			e.addInfo("file", filename);
			// ----
			throw e;
		}
	}

	public void setLocalFileVersion(int version)
	{
		this.localFileVersion = version;
	}

	public TimeStamp getTimeStamp()
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
	public String getMetaFilename()
	{
		return filename;
	}
}
