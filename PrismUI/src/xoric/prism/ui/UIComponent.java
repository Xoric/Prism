package xoric.prism.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;

public abstract class UIComponent implements IDrawableUI, IUISubcomponent, IActiveUI, IStackable
{
	private final UIIdentifier identifier;

	protected Ruler xRuler;
	protected Ruler yRuler;
	protected Ruler widthRuler;

	protected final FloatRect rect;
	protected boolean isMouseDown;
	protected boolean hasFocus;

	private final List<IUISubcomponent> children;

	public UIComponent(UIIdentifier id)
	{
		this.identifier = id;

		children = new ArrayList<IUISubcomponent>();

		rect = new FloatRect();
		rect.setHeight(28.0f);

		xRuler = new Ruler();
		yRuler = new Ruler();
		widthRuler = new Ruler();
	}

	public UIIdentifier getIdentifier()
	{
		return identifier;
	}

	public IFloatRect_r getCurrentRect()
	{
		return rect;
	}

	protected void registerChild(IUISubcomponent c)
	{
		children.add(c);
	}

	protected void unregisterChild(IUISubcomponent c)
	{
		children.remove(c);
	}

	public void setXRuler(float constant, float factor)
	{
		xRuler.constant = constant;
		xRuler.factor = factor;
	}

	public void setYRuler(float constant, float factor)
	{
		yRuler.constant = constant;
		yRuler.factor = factor;
	}

	public void setWidthRuler(float constant, float factor)
	{
		widthRuler.constant = constant;
		widthRuler.factor = factor;
	}

	public Ruler getXRuler()
	{
		return xRuler;
	}

	public Ruler getYRuler()
	{
		return yRuler;
	}

	public Ruler getWidthRuler()
	{
		return widthRuler;
	}

	@Override
	public boolean containsMouse(IFloatPoint_r mouse)
	{
		return rect.contains(mouse);
	}

	public final IFloatPoint_r getCurrentPosition()
	{
		return rect.getTopLeft();
	}

	public final float getCurrentWidth()
	{
		return rect.getWidth();
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		if (parentRect != null)
		{
			rect.setX(parentRect.getX() + xRuler.calculate(parentRect.getWidth()));
			rect.setY(parentRect.getY() + yRuler.calculate(parentRect.getHeight()));
			rect.setWidth(widthRuler.calculate(parentRect.getWidth()));

			rearrangeChildrenOnly();
		}
	}

	protected void rearrangeChildrenOnly()
	{
		for (IUISubcomponent c : children)
			c.rearrange(rect);
	}

	protected Integer importInt()
	{
		return null;
	}

	protected Text importText()
	{
		return null;
	}

	@Override
	public final IActiveUI mouseDown(IFloatPoint_r mouse)
	{
		if (containsMouse(mouse))
		{
			IActiveUI a = mouseDownConfirmed(mouse);
			if (a != null)
			{
				if (a == this)
					isMouseDown = true;

				return a;
			}
		}
		return null;
	}

	@Override
	public final void mouseUp()
	{
		isMouseDown = false;
		mouseUpConfirmed();
	}

	protected abstract IActiveUI mouseDownConfirmed(IFloatPoint_r mouse);

	protected abstract void mouseUpConfirmed();

	//	@Override
	//	public void pack(OutputStream stream) throws IOException, PrismException
	//	{
	//		xRuler.pack(stream);
	//		yRuler.pack(stream);
	//		widthRuler.pack(stream);
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		xRuler.unpack(stream);
	//		yRuler.unpack(stream);
	//		widthRuler.unpack(stream);
	//	}

	@Override
	public void moveBy(float dx, float dy)
	{
		rect.addPosition(dx, dy);

		for (IUISubcomponent c : children)
			c.moveBy(dx, dy);
	}

	@Override
	public void appendTo(Heap_out h)
	{
		xRuler.appendTo(h);
		yRuler.appendTo(h);
		widthRuler.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		xRuler.extractFrom(h);
		yRuler.extractFrom(h);
		widthRuler.extractFrom(h);
	}

	@Override
	public void setFocus(boolean hasFocus)
	{
		this.hasFocus = hasFocus;
	}
}
