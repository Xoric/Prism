package xoric.prism.client.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public abstract class UIComponentH extends UIComponent
{
	protected Ruler heightRuler;

	public UIComponentH()
	{
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

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		super.unpack(stream);

		heightRuler.unpack(stream);
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		super.pack(stream);

		heightRuler.pack(stream);
	}
}