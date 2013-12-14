package xoric.prism.meta;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.IPackable;
import xoric.prism.data.IntPacker;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.exceptions.PrismMetaFileException;

/**
 * @author Felix M�hrle
 * @since 26.06.2011, 15:35:16
 */
public class AttachmentLoader implements IActor, IPackable
{
	private static final int FILLER = 0x20000000;

	private final File file;
	private final List<Integer> attachmentSizes;
	private final IntPacker intPacker;
	private int attachmentStart;

	/**
	 * AttachmentLoader constructor.
	 * @param file
	 */
	public AttachmentLoader(File file)
	{
		this.file = file;
		this.attachmentSizes = new ArrayList<Integer>();
		this.intPacker = new IntPacker();
	}

	/**
	 * @return Number of attachments in the file.
	 */
	public int getAttachmentCount()
	{
		return attachmentSizes.size();
	}

	/**
	 * @return File
	 */
	public File getFile()
	{
		return file;
	}

	public void setAttachmentStart(int attachmentStart)
	{
		this.attachmentStart = attachmentStart;
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// write number of attachments
		intPacker.setValue(attachmentSizes.size());
		intPacker.pack(stream);

		// write attachment sizes
		for (int i = 0; i < attachmentSizes.size(); ++i)
		{
			intPacker.setValue(attachmentSizes.get(i));
			intPacker.pack(stream);
		}

		// write attachment start
		intPacker.setValue(attachmentStart | FILLER); // ensure 4 bytes are being used 
		intPacker.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		// read number of attachments
		intPacker.unpack(stream);
		int n = intPacker.getValue();

		// read attachment sizes
		attachmentSizes.clear();
		for (int i = 0; i < n; ++i)
		{
			intPacker.unpack(stream);
			int size = intPacker.getValue();
			attachmentSizes.add(size);
		}

		// read attachment start
		intPacker.unpack(stream);
		attachmentStart = intPacker.getValue() & ~FILLER;
	}

	@Override
	public int getPackedSize()
	{
		// number of attachments
		intPacker.setValue(attachmentSizes.size());
		int size = intPacker.getPackedSize();

		// attachment sizes
		for (int i = 0; i < attachmentSizes.size(); ++i)
		{
			intPacker.setValue(attachmentSizes.get(i));
			size += intPacker.getPackedSize();
		}

		// attachment start
		size += 4;

		return size;
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

	/**
	 * Calculates and returns the start position of the requested attachment.
	 * @param attachmentIndex
	 * @return int
	 * @throws PrismMetaFileException
	 */
	private int calcAttachmentStart(int attachmentIndex) throws PrismMetaFileException
	{
		int start = 0;

		if (attachmentIndex >= 0 && attachmentIndex < attachmentSizes.size())
		{
			start = attachmentStart;

			for (int i = 0; i < attachmentIndex; ++i)
				start += attachmentSizes.get(i);
		}
		else
		{
			ErrorCode c = new ErrorCode(this, ErrorID.ATTACHMENT_NOT_FOUND);
			PrismMetaFileException e = new PrismMetaFileException(c, file);
			e.appendInfo("attachmentIndex", String.valueOf(attachmentIndex));
			throw e;
		}
		return start;
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.ATTACHMENT_LOADER;
	}
}