package xoric.prism.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public abstract class UIComponentH extends UIComponent
{
	protected Ruler heightRuler;

	public UIComponentH(UIIdentifier id)
	{
		super(id);

		heightRuler = new Ruler();
	}

	public final IFloatPoint_r getCurrentSize()
	{
		return rect.getSize();
	}

	public final float getCurrentHeight()
	{
		return rect.getHeight();
	}

	public final Ruler getHeightRuler()
	{
		return heightRuler;
	}

	public void setHeightRuler(float constant, float factor)
	{
		heightRuler.constant = constant;
		heightRuler.factor = factor;
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		if (parentRect != null)
		{
			rect.setHeight(heightRuler.calculate(parentRect.getHeight()));
			super.rearrange(parentRect);
		}
	}

	//	@Override
	//	public void pack(OutputStream stream) throws IOException, PrismException
	//	{
	//		super.pack(stream);
	//
	//		heightRuler.pack(stream);
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		super.unpack(stream);
	//
	//		heightRuler.unpack(stream);
	//	}

	@Override
	public void appendTo(Heap_out h)
	{
		super.appendTo(h);
		heightRuler.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		super.extractFrom(h);
		heightRuler.extractFrom(h);
	}
}
