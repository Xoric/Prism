package xoric.prism.client.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.materials.Printer;

public class UiTextArea extends FloatRect implements IDrawableUI
{
	private Text text;
	private float scale;
	private final List<Integer> stops;

	public UiTextArea()
	{
		stops = new ArrayList<Integer>();
		scale = Printer.DEFAULT_SCALE;
	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}

	public void setText(IText_r text)
	{
		this.text = new Text(text);
	}

	public void setText(Text text)
	{
		this.text = text;
	}

	public void arrangeLines()
	{
		stops.clear();

		if (text != null)
		{
			boolean resume = true;
			int index = -1;
			int lastIndex = 0;
			float width = 0.0f;

			do
			{
				index = text.findSeparator(index + 1);

				if (index < 0)
				{
					resume = false;
					index = text.length() - 1;
				}

				// calculate line length
				float w = 0.0f;
				for (int i = lastIndex; i < index + 1; ++i)
					w += Materials.printer.getWidth(text.symbolAt(i), scale);

				width += w;

				// check if current word still fits into this line
				if (width >= size.x)
				{
					stops.add(lastIndex);
					width = w;
				}
				lastIndex = index;
			}
			while (resume);
		}

	}

	public void test()
	{
		setSize(100.0f, 80.0f);
		setText(new Text(
				"Oppositionsführer Klitschko hat Ukraines Präsidenten Janukowitsch attackiert. Der beute das Land schamlos aus. Die Verhandlungen mit einem Betrüger fielen ihm schwer."));
		arrangeLines();

		for (Integer i : stops)
			System.out.println(i);

		int lasti = 0;
		for (Integer i : stops)
		{
			System.out.println(text.substring(lasti, i + 1));
			lasti = i;
		}
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		// TODO Auto-generated method stub

	}
}
