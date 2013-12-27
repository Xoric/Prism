package xoric.prism.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import xoric.prism.data.IntPacker;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.exceptions.PrismMetaFileException;

public class MetaFile implements IActor
{
	private final File file;
	private int localFileVersion;
	private TimeStamp timeStamp;
	private MetaList metaList;
	private AttachmentLoader attachmentLoader;

	public MetaFile(File file)
	{
		this.file = file;
		this.localFileVersion = 0;
		this.timeStamp = new TimeStamp();
		this.metaList = new MetaList();
	}

	public void setMetaList(MetaList metaList)
	{
		this.metaList = metaList;
	}

	public void load() throws PrismMetaFileException
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
			attachmentLoader = new AttachmentLoader(file, attachmentCount);
			attachmentLoader.unpack(stream);

			// close stream
			stream.close();
		}
		catch (Exception e0)
		{
			ErrorID id = e0 instanceof FileNotFoundException ? ErrorID.READ_ERROR : ErrorID.COMMON;
			ErrorCode c = new ErrorCode(this, id);
			PrismMetaFileException e = new PrismMetaFileException(c, file);
			e.appendOriginalException(e0);
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

	public AttachmentLoader getAttachmentLoader()
	{
		return attachmentLoader;
	}

	public MetaList getMetaList()
	{
		return metaList;
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.META_FILE;
	}

	public int getLocalFileVersion()
	{
		return localFileVersion;
	}
}
