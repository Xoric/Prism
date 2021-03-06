package xoric.prism.data.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.packable.IPredictablePackable;

public class AttachmentTable implements IPredictablePackable
{
	private final AttachmentHeader[] attachments;

	public AttachmentTable(int count)
	{
		attachments = new AttachmentHeader[count];
	}

	public void set(int index, AttachmentHeader h)
	{
		attachments[index] = h;
	}

	public int getAttachmentCount()
	{
		return attachments == null ? 0 : attachments.length;
	}

	public AttachmentHeader get(int index)
	{
		return index >= 0 && index < attachments.length ? attachments[index] : null;
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
	public int calcPackedSize()
	{
		int size = 0;

		// attachment headers
		for (int i = 0; i < attachments.length; ++i)
			size += attachments[i].calcPackedSize();

		return size;
	}
}
