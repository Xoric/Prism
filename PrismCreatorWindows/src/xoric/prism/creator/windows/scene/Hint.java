package xoric.prism.creator.windows.scene;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.tools.AllTools;

public class Hint implements IDrawableUI
{
	private static final int DURATION = 1400;

	private Text text;
	private IFloatPoint_r pos;
	private PrismColor color;
	private int age;

	public Hint(Text text, IFloatPoint_r pos)
	{
		this.text = text;
		this.pos = pos;
		this.color = new PrismColor();
		this.color.set(255, 255, 255, 255);
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		AllTools.printer.setText(text);
		AllTools.printer.setColor(color);
		AllTools.printer.print(pos);
		AllTools.printer.resetColor();
	}

	public boolean update(int passedMs)
	{
		age += passedMs;
		int i = DURATION - age;
		float f = i / (float) DURATION;
		int a = (int) (f * 255);
		boolean b;

		if (a <= 0)
		{
			a = 0;
			b = false;
		}
		else
			b = true;

		color.setAlpha(a);

		return b;
	}
}
