package xoric.prism.creator.custom.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.packable.TextPacker;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Rect;
import xoric.prism.data.types.Text;

public class ObjectModel implements IPackable, IStackable
{
	private Text name;
	private List<Rect> rects;

	public ObjectModel()
	{
		this.name = new Text("NEW OBJECT");
		this.rects = new ArrayList<Rect>();
	}

	public ObjectModel(Text name)
	{
		setName(name);
		this.rects = new ArrayList<Rect>();
	}

	public void setName(Text name)
	{
		this.name = name;
	}

	public int getRectCount()
	{
		return rects.size();
	}

	public Rect getRect(int index)
	{
		return rects.get(index);
	}

	public void addRect(Rect rect)
	{
		rects.add(rect);
	}

	public IText_r getName()
	{
		return name;
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		name = TextPacker.unpack_s(stream);
		int n = IntPacker.unpack_s(stream);

		for (int i = 0; i < n; ++i)
		{
			Rect r = new Rect();
			r.unpack(stream);
			addRect(r);
		}
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		TextPacker.pack_s(stream, name);
		int n = rects.size();
		IntPacker.pack_s(stream, n);

		for (int i = 0; i < n; ++i)
			rects.get(i).pack(stream);
	}

	public void removeRect(int index)
	{
		rects.remove(index);
	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.texts.add(name);
		h.ints.add(rects.size());

		for (Rect r : rects)
			r.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		rects.clear();

		name = h.nextText();
		final int n = h.nextInt();

		for (int i = 0; i < n; ++i)
		{
			Rect r = new Rect();
			r.extractFrom(h);
			rects.add(r);
		}
	}
}
