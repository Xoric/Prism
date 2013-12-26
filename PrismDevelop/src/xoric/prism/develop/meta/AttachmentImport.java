package xoric.prism.develop.meta;

import java.io.File;
import java.io.FileInputStream;

import xoric.prism.compression.BufferCompressor;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.exceptions.PrismDevException;

class AttachmentImport implements IActor
{
	private static final double COMPRESSION_THRESHOLD = 0.8;

	private final File file;
	private byte[] content;
	private boolean isCompressed;

	public AttachmentImport(File file)
	{
		this.file = file;
		this.content = null;
		this.isCompressed = false;
	}

	public byte[] getContent()
	{
		return content;
	}

	public boolean isCompressed()
	{
		return isCompressed;
	}

	public void importAttachment() throws PrismDevException
	{
		// check file size
		int originalSize = (int) file.length();
		if (originalSize <= 0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.FILE_NOT_FOUND);
			PrismDevException e = new PrismDevException(c);
			e.appendInfo("file", file.toString());
			throw e;
		}

		// read file content
		byte[] content;
		try
		{
			content = new byte[originalSize];
			FileInputStream in = new FileInputStream(file);
			in.read(content);
			in.close();
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.READ_ERROR);
			PrismDevException e = new PrismDevException(c);
			e.appendOriginalException(e0);
			e.appendInfo("file", file.toString());
			throw e;
		}

		// check if content can be compressed
		boolean useCompression;
		byte[] compressed;
		try
		{
			compressed = BufferCompressor.compressBuffer(content);
			int packedSize = compressed.length;
			double rate = (double) packedSize / (double) originalSize;
			useCompression = rate < COMPRESSION_THRESHOLD;
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.COMPRESSION_ERROR);
			PrismDevException e = new PrismDevException(c);
			e.appendOriginalException(e0);
			e.appendInfo("file", file.toString());
			throw e;
		}

		// store file content
		if (useCompression)
		{
			this.content = compressed;
			this.isCompressed = true;
		}
		else
		{
			this.content = content;
			this.isCompressed = false;
		}
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.ATTACHMENT_WRITER;
	}
}
