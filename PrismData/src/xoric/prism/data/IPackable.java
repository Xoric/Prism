package xoric.prism.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IPackable
{
	public void pack(OutputStream stream) throws IOException;

	public void unpack(InputStream stream) throws IOException;

	/**
	 * Get packed size in bytes.
	 * @return int
	 */
	public int getPackedSize();
}