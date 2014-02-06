package xoric.prism.client.ui.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

public class ButtonActionMask implements IPackable
{
	private final List<Integer> intSources;
	private final List<Integer> textSources;

	public ButtonActionMask()
	{
		intSources = new ArrayList<Integer>();
		textSources = new ArrayList<Integer>();
	}

	public List<Integer> getIntSources()
	{
		return intSources;
	}

	public List<Integer> getTextSources()
	{
		return textSources;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		intSources.clear();
		textSources.clear();

		int n = IntPacker.unpack_s(stream);
		IntPacker.unpack_s(stream, n, intSources);

		n = IntPacker.unpack_s(stream);
		IntPacker.unpack_s(stream, n, textSources);
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, intSources.size());
		IntPacker.pack_s(stream, intSources);

		IntPacker.pack_s(stream, textSources.size());
		IntPacker.pack_s(stream, textSources);
	}
}
