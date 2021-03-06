package xoric.prism.develop.meta;

import java.io.File;
import java.io.FileInputStream;

import xoric.prism.data.compression.BufferCompressor;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.AttachmentHeader;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Text;

class AttachmentImporter
{
	private static final double COMPRESSION_THRESHOLD = 0.85;

	private final File file;
	private boolean isCompressed;
	private byte[] content;

	public AttachmentImporter(IPath_r path, String filename)
	{
		this.file = path.getFile(filename);
	}

	public byte[] getContent()
	{
		return content;
	}

	public void importAttachment() throws PrismException
	{
		// check file size
		int originalSize = (int) file.length();
		if (originalSize <= 0)
		{
			PrismException e = new PrismException();
			// ----
			// ----
			// ----
			e.setText("An attachment file is empty or does not exist.");
			e.addInfo("file", file.toString());
			// ----
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
			PrismException e = new PrismException(e0);
			// ----
			// ----
			// ----
			e.setText("An error occured while trying to import an attachment.");
			e.addInfo("file", file.toString());
			// ----
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
			PrismException e = new PrismException(e0);
			// ----
			// ----
			// ----
			e.setText("An error occured while compressing an attachment.");
			e.addInfo("file", file.toString());
			// ----
			throw e;
		}

		// store file content
		this.isCompressed = useCompression;

		if (useCompression)
			this.content = compressed;
		else
			this.content = content;
	}

	public int getSize()
	{
		return content.length;
	}

	public AttachmentHeader createHeader()
	{
		Text name = new Text(file.getName());
		AttachmentHeader h = new AttachmentHeader(name, isCompressed, 0, content.length);

		return h;
	}
}
