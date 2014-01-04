package xoric.prism.data.meta;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import xoric.prism.data.exceptions.PrismMetaFileException;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.data.types.IPath_r;

/**
 * @author Felix M�hrle
 * @since 26.06.2011, 15:35:16
 */
public class AttachmentLoader extends AttachmentTable implements IActor
{
	private final File file;
	private final String filename;

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
	 * @throws PrismMetaFileException
	 */
	public byte[] loadAttachment(int attachmentIndex) throws PrismMetaFileException
	{
		// get AttachmentHeader
		AttachmentHeader h = get(attachmentIndex);
		boolean isIndexValid = h != null && h.getContentStart() > 0 && h.getContentSize() > 0;

		if (!isIndexValid)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.ATTACHMENT_NOT_FOUND);
			PrismMetaFileException e = new PrismMetaFileException(c, filename);
			e.appendInfo("index", String.valueOf(attachmentIndex));
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
			ErrorCode c = new ErrorCode(this, ErrorID.READ_ERROR);
			PrismMetaFileException e = new PrismMetaFileException(c, filename);
			e.appendOriginalException(e0);
			e.appendInfo("index", String.valueOf(attachmentIndex));
			throw e;
		}
		return buf;
	}

	/**
	 * Loads an attachment as String-Array.
	 * @param attachmentIndex
	 * @return String[]
	 * @throws PrismMetaFileException
	 */
	public String[] loadAttachmentAsStringArray(int attachmentIndex) throws PrismMetaFileException
	{
		byte[] buf = loadAttachment(attachmentIndex);
		ByteArrayInputStream stream = new ByteArrayInputStream(buf);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder lines = new StringBuilder();

		try
		{
			String line;
			while ((line = br.readLine()) != null)
				lines.append(line + '\n');
			stream.close();
		}
		catch (Exception e0)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.READ_ERROR);
			PrismMetaFileException e = new PrismMetaFileException(c, filename);
			e.appendInfo("attachmentIndex", String.valueOf(attachmentIndex));
			throw e;
		}

		String[] result = new String[] { lines.toString() };
		return result;
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.ATTACHMENT_LOADER;
	}
}