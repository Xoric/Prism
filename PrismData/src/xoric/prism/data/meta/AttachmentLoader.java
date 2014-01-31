package xoric.prism.data.meta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IPath_r;

/**
 * @author Felix Möhrle
 * @since 26.06.2011, 15:35:16
 */
public class AttachmentLoader extends AttachmentTable
{
	private final File file;
	private final String filename;
	private final byte[] buffer = new byte[1024];

	public AttachmentLoader(IPath_r path, String filename, int count)
	{
		super(count);

		this.file = path.getFile(filename);
		this.filename = filename;
	}

	/**
	 * Loads an attachment from a MetaFile.
	 * @param attachmentIndex
	 * @return ByteArrayInputStream
	 * @throws IOException
	 * @throws PrismException
	 */
	public byte[] loadAttachment(int attachmentIndex) throws PrismException
	{
		// get AttachmentHeader
		AttachmentHeader h = get(attachmentIndex);
		boolean isIndexValid = h != null && h.getContentStart() > 0 && h.getContentSize() > 0;

		if (!isIndexValid)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("non-existing attachment requested");
			e.code.addInfo("attachmentIndex", attachmentIndex);
			e.code.addInfo("attachmentCount", getAttachmentCount());
			// ----
			e.addInfo("file", filename);
			// ----
			throw e;
		}

		// open file and load attachment content
		byte[] buf = null;
		try
		{
			FileInputStream fin = new FileInputStream(file);
			buf = new byte[h.getContentSize()];
			fin.getChannel().position(h.getContentStart());
			fin.read(buf);
			fin.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("error while reading attachment buffer");
			e.code.addInfo("attachmentIndex", attachmentIndex);
			e.code.addInfo("attachmentHeader", h.toString());
			// ----
			e.addInfo("file", filename);
			// ----			
			throw e;
		}

		// decompress attachment buffer if compressed
		if (h.isCompressed())
		{
			try
			{
				buf = decompressBuffer(buf);
			}
			catch (Exception e0)
			{
				PrismException e = new PrismException(e0);
				// ----
				e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
				// ----
				e.code.setText("error while decompressing attachment buffer");
				e.code.addInfo("attachmentIndex", attachmentIndex);
				e.code.addInfo("attachmentHeader", h.toString());
				// ----
				e.addInfo("file", filename);
				// ----			
				throw e;
			}
		}
		return buf;
	}

	private byte[] decompressBuffer(byte[] buf) throws IOException
	{
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		GZIPInputStream zin = new GZIPInputStream(in);

		int bytes;
		while ((bytes = zin.read(buffer)) > 0)
			result.write(buffer, 0, bytes);

		zin.close();
		in.close();

		return result.toByteArray();
	}

	public File getFile()
	{
		return file;
	}
}
