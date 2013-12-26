package xoric.prism.meta;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.exceptions.PrismMetaFileException;

/**
 * @author Felix M�hrle
 * @since 26.06.2011, 15:35:16
 */
public class AttachmentLoader extends AttachmentTable implements IActor
{
	private final File file;

	/**
	 * AttachmentLoader constructor.
	 * @param file
	 */
	public AttachmentLoader(File file)
	{
		this.file = file;
	}

	/**
	 * @return File
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Loads an attachment from a MetaFile.
	 * @param attachmentIndex
	 * @return ByteArrayInputStream
	 * @throws IOException
	 * @throws PrismMetaFileException
	 */
	public ByteArrayInputStream loadAttachment(int attachmentIndex) throws PrismMetaFileException
	{
		boolean isIndexValid = attachmentIndex >= 0 && attachmentIndex < getAttachmentCount();
		ByteArrayInputStream stream = null;

		if (isIndexValid)
		{
			// get start position and size of the attachment
			int start = calcAttachmentStart(attachmentIndex);
			int size = attachmentSizes.get(attachmentIndex);
			isIndexValid &= start > 0 && size > 0;

			if (isIndexValid)
			{
				try
				{
					// open file and load attachment
					FileInputStream fin = new FileInputStream(file);
					byte[] buf = new byte[size];
					fin.getChannel().position(start);
					fin.read(buf);
					fin.close();
					stream = new ByteArrayInputStream(buf);
				}
				catch (Exception e0)
				{
					ErrorCode c = new ErrorCode(this, ErrorID.READ_ERROR);
					PrismMetaFileException e = new PrismMetaFileException(c, file);
					e.appendOriginalException(e0);
					e.appendInfo("attachmentIndex", String.valueOf(attachmentIndex));
					throw e;
				}
			}
		}

		if (stream == null)
		{
			ErrorCode c = new ErrorCode(this, ErrorID.ATTACHMENT_NOT_FOUND);
			PrismMetaFileException e = new PrismMetaFileException(c, file);
			e.appendInfo("attachmentIndex", String.valueOf(attachmentIndex));
			throw e;
		}
		return stream;
	}

	/**
	 * Loads an attachment as String-Array.
	 * @param attachmentIndex
	 * @return String[]
	 * @throws PrismMetaFileException
	 */
	public String[] loadAttachmentAsStringArray(int attachmentIndex) throws PrismMetaFileException
	{
		ByteArrayInputStream stream = loadAttachment(attachmentIndex);
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
			PrismMetaFileException e = new PrismMetaFileException(c, file);
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
