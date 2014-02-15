package xoric.prism.data.packable;

import java.io.IOException;
import java.io.InputStream;

import xoric.prism.data.exceptions.PrismException;

public interface IPackable_in
{
	public void unpack(InputStream stream) throws IOException, PrismException;
}
