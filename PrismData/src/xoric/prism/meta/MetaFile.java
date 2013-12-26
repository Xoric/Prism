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
	private MetaList metaList;
	private final AttachmentLoader attachmentLoader;
	private final IntPacker intPacker;
	private int localFileVersion;

	public MetaFile(File file)
	{
		this.file = file;
		this.localFileVersion = 0;
		this.metaList = new MetaList();
		this.attachmentLoader = new AttachmentLoader(file);
		this.intPacker = new IntPacker();
	}

	public void setMetaList(MetaList metaList)
	{
		this.metaList = metaList;
	}

	public void load() throws PrismMetaFileException
	{
		try
		{
			FileInputStream stream = new FileInputStream(file);

			// read localFileVersion
			intPacker.unpack(stream);
			localFileVersion = intPacker.getValue();

			// read MetaList and AttachmentLoader
			metaList.unpack(stream);
			attachmentLoader.unpack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			ErrorID id = e0 instanceof FileNotFoundException ? ErrorID.FILE_NOT_FOUND : ErrorID.COMMON;
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
