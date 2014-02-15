package xoric.prism.data.packable;

import java.io.IOException;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;

public interface IPackable_out
{
	public void pack(OutputStream stream) throws IOException, PrismException;
}
