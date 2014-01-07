package xoric.prism.data.meta;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import xoric.prism.data.exceptions2.PrismException2;
import xoric.prism.data.exceptions2.UserErrorText;
import xoric.prism.data.types.IPath_r;

/**
 * @author Felix M�hrle
 * @since 26.06.2011, 15:35:16
 */
public class AttachmentLoader extends AttachmentTable
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
	 * @throws PrismException2
	 */
	public byte[] loadAttachment(int attachmentIndex) throws PrismException2
	{
		// get AttachmentHeader
		AttachmentHeader h = get(attachmentIndex);
		boolean isIndexValid = h != null && h.getContentStart() > 0 && h.getContentSize() > 0;

		if (!isIndexValid)
		{
			PrismException2 e = new PrismException2();
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
			PrismException2 e = new PrismException2(e0);
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
		return buf;
	}

	/**
	 * Loads an attachment as String-Array.
	 * @param attachmentIndex
	 * @return String[]
	 * @throws PrismMetaFileException
	 */
	public String[] loadAttachmentAsStringArray(int attachmentIndex) throws PrismException2
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
			PrismException2 e = new PrismException2(e0);
			// ----
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			// ----
			e.code.setText("error while converting attachment buffer to string array");
			e.code.addInfo("attachmentIndex", attachmentIndex);
			e.code.addInfo("bufferLength", buf.length);
			// ----
			e.addInfo("file", filename);
			// ----			
			throw e;
		}

		String[] result = new String[] { lines.toString() };
		return result;
	}
}
