package xoric.prism.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import xoric.prism.data.IntPacker;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.exceptions.PrismMetaFileException;

public class MetaFile implements IActor
{
	private final File file;
	private final MetaList metaList;
	private final AttachmentLoader attachmentLoader;
	private final IntPacker intPacker;

	public MetaFile(File file)
	{
		this.file = file;
		this.metaList = new MetaList();
		this.attachmentLoader = new AttachmentLoader(file);
		this.intPacker = new IntPacker();
	}

	public void load() throws PrismMetaFileException
	{
		try
		{
			FileInputStream stream = new FileInputStream(file);
			metaList.unpack(stream);
			attachmentLoader.unpack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			ErrorID id = e0 instanceof FileNotFoundException ? ErrorID.FILE_NOT_FOUND : ErrorID.GENERIC;
			ErrorCode c = new ErrorCode(this, id);
			PrismMetaFileException e = new PrismMetaFileException(c, file);
			e.appendOriginalException(e0);
			throw e;
		}
	}

	public void write() throws PrismMetaFileException
	{
		try
		{
			// overwrite file
			if (file.exists())
				file.delete();
			FileOutputStream stream = new FileOutputStream(file);

			// write metaList 
			metaList.pack(stream);

			// update start of attachments and write attachmentLoader
			int attachmentStart = (int) file.length() + attachmentLoader.getPackedSize();
			attachmentLoader.setAttachmentStart(attachmentStart);
			attachmentLoader.pack(stream);

			// close stream
			stream.close();
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.WRITE_ERROR);
			PrismMetaFileException e = new PrismMetaFileException(c, file);
			e.appendOriginalException(e0);
			throw e;
		}
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
}