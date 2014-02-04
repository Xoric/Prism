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
	private final List<Text> lines;
	private float scale;

	public UiTextArea()
	{
		lines = new ArrayList<Text>();
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
		lines.clear();

		if (text != null)
		{
			float w = 0.0f;
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < text.length(); ++i)
			{
				char c = text.charAt(i);
				int s = text.symbolAt(i);

				w += Materials.printer.getWidth(s, scale);
				sb.append(c);

				// TODO: resume
			}
		}
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		// TODO Auto-generated method stub

	}
}
