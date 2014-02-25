package xoric.prism.ui.button;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;

public class ButtonActionMask implements IStackable
{
	private List<Integer> intSources;
	private List<Integer> textSources;

	public ButtonActionMask()
	{
		intSources = new ArrayList<Integer>();
		textSources = new ArrayList<Integer>();
	}

	public void setIntSources(List<Integer> sources)
	{
		intSources = sources;
	}

	public void setTextSources(List<Integer> sources)
	{
		textSources = sources;
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
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("i:[");
		for (int i = 0; i < intSources.size(); ++i)
		{
			if (i > 0)
				sb.append(",");

			sb.append(intSources.get(i));
		}
		sb.append("], t:[");

		for (int i = 0; i < textSources.size(); ++i)
		{
			if (i > 0)
				sb.append(",");

			sb.append(textSources.get(i));
		}
		sb.append("]");

		return sb.toString();
	}

	//	@Override
	//	public void pack(OutputStream stream) throws IOException
	//	{
	//		IntPacker.pack_s(stream, intSources.size());
	//		IntPacker.pack_s(stream, intSources);
	//
	//		IntPacker.pack_s(stream, textSources.size());
	//		IntPacker.pack_s(stream, textSources);
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		intSources.clear();
	//		textSources.clear();
	//
	//		int n = IntPacker.unpack_s(stream);
	//		IntPacker.unpack_s(stream, n, intSources);
	//
	//		n = IntPacker.unpack_s(stream);
	//		IntPacker.unpack_s(stream, n, textSources);
	//	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add(intSources.size());
		h.ints.addAll(intSources);

		h.ints.add(textSources.size());
		h.ints.addAll(textSources);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		intSources.clear();
		textSources.clear();

		int n = h.nextInt();
		for (int i = 0; i < n; ++i)
			intSources.add(h.nextInt());

		n = h.nextInt();
		for (int i = 0; i < n; ++i)
			textSources.add(h.nextInt());
	}
}
