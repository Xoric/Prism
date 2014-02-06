package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;

public interface IPackable
{
	public void unpack(InputStream stream) throws IOException, PrismException;

	public void pack(OutputStream stream) throws IOException;
}
