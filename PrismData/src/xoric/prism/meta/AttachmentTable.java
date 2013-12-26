package xoric.prism.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.AttachmentHeader;
import xoric.prism.data.IPackable;

public class AttachmentTable implements IPackable
{
	private final AttachmentHeader[] attachments;

	public AttachmentTable(int count)
	{
		attachments = new AttachmentHeader[count];
	}

	public int getCount()
	{
		return attachments == null ? 0 : attachments.length;
	}

	public AttachmentHeader getAttachmentHeader(int index)
	{
		return attachments[index];
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		// pack attachment headers
		for (int i = 0; i < attachments.length; ++i)
			attachments[i].pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		// unpack attachment headers
		for (int i = 0; i < attachments.length; ++i)
		{
			AttachmentHeader a = new AttachmentHeader();
			a.unpack(stream);
			attachments[i] = a;
		}
	}

	@Override
	public int getPackedSize()
	{
		int size = 0;

		// attachment headers
		for (int i = 0; i < attachments.length; ++i)
			size += attachments[i].getPackedSize();

		return size;
	}
}
