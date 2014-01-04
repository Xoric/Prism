package xoric.prism.data.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import xoric.prism.data.exceptions.PrismMetaFileException;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IntPacker;

public class MetaFile implements IActor, IMetaListOwner
{
	private final File file;
	private final IPath_r path;
	private final String filename;
	private int localFileVersion;
	private TimeStamp timeStamp;
	private MetaList metaList;
	private AttachmentLoader attachmentLoader;

	//	public MetaFile(String filename)
	//	{
	//		this.filename = filename;
	//		this.path = Prism.global.getDataPath();
	//		this.file = this.path.getFile(filename);
	//		this.localFileVersion = 0;
	//		this.timeStamp = new TimeStamp();
	//		this.metaList = new MetaList();
	//		this.metaList.setOwner(this);
	//	}

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
			attachmentLoader = new AttachmentLoader(path, filename, attachmentCount);
			attachmentLoader.unpack(stream);

			// close stream
			stream.close();
		}
		catch (Exception e0)
		{
			e0.printStackTrace();

			ErrorID id = e0 instanceof FileNotFoundException ? ErrorID.READ_ERROR : ErrorID.COMMON;
			ErrorCode c = new ErrorCode(this, id);
			PrismMetaFileException e = new PrismMetaFileException(c, filename);
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

	@Override
	public ActorID getActorID()
	{
		return ActorID.META_FILE;
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