package xoric.prism.client.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.materials.Printer;

public class UITextArea extends FloatRect implements IDrawableUI, IUITextComponent
{
	private Text text;
	private float scale;
	private final List<Integer> stops;
	private final FloatPoint tempPos;

	public UITextArea()
	{
		stops = new ArrayList<Integer>();
		scale = Printer.DEFAULT_SCALE;
		tempPos = new FloatPoint();
	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}

	public void setText(IText_r text)
	{
		this.text = new Text(text);
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
				if (width >= size.x)
				{
					stops.add(lastIndex);
					width = w;
				}
				lastIndex = index + 1;
			}
			while (index >= 0);

			int z = stops.get(stops.size() - 1);
			if (z < text.length())
				stops.add(text.length());
		}
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		tempPos.copyFrom(topLeft);
		Materials.printer.setText(text);
		int start = 0;

		for (int i = 0; i < stops.size(); ++i)
		{
			int j = stops.get(i);

			Materials.printer.setOnset(start, j);
			Materials.printer.print(tempPos);
			tempPos.y += Materials.printer.getHeight(scale);

			start = j;
		}
	}

	@Override
	public IText_r getText()
	{
		return text;
	}
}
