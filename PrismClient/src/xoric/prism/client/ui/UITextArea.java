package xoric.prism.client.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

public class UITextArea implements IDrawableUI, IUITextComponent, IUISubcomponent
{
	private static final float BORDER = 20.0f;

	private Text text;
	//	private float scale;
	private final FloatRect rect;
	private final List<Integer> stops;
	private final FloatPoint tempPos;

	public UITextArea()
	{
		text = new Text("");
		rect = new FloatRect();
		stops = new ArrayList<Integer>();
		//		scale = Printer.DEFAULT_SCALE;
		tempPos = new FloatPoint();
	}

	//	public void setScale(float scale)
	//	{
	//		this.scale = scale;
	//	}

	public void setText(IText_r text)
	{
		this.text.set(text);
	}

	@Override
	public void setText(Text text)
	{
		this.text = text;
	}

	public void rearrangeLines()
	{
		stops.clear();

		if (text != null)
		{
			int index = -1;
			int lastIndex = 0;
			float width = 0.0f;

			do
			{
				index = text.findSeparator(index + 1);
				float w = Materials.printer.calcTextWidth(text, lastIndex, index);
				width += w;

				// check if current word still fits into the current line
				if (width >= rect.getWidth())
				{
					stops.add(lastIndex);
					width = w;
				}
				lastIndex = index + 1;
			}
			while (index >= 0);

			if (stops.size() == 0)
				stops.add(text.length());
			else
			{
				int z = stops.get(stops.size() - 1);
				if (z < text.length())
					stops.add(text.length());
			}
		}
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		tempPos.copyFrom(rect.getTopLeft());
		Materials.printer.setText(text);
		int start = 0;

		for (int i = 0; i < stops.size(); ++i)
		{
			int j = stops.get(i);

			Materials.printer.setOnset(start, j);
			Materials.printer.print(tempPos);
			tempPos.y += Materials.printer.getHeight();

			start = j;
		}
	}

	@Override
	public IText_r getText()
	{
		return text;
	}

	@Override
	public void moveBy(float dx, float dy)
	{
		rect.addPosition(dx, dy);
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		if (parentRect != null)
		{
			rect.setX(parentRect.getX() + BORDER);
			rect.setY(parentRect.getY() + BORDER);
			rect.setWidth(parentRect.getWidth() - 2.0f * BORDER);
			rect.setHeight(parentRect.getHeight() - 2.0f * BORDER);

			rearrangeLines();
		}
	}
}
