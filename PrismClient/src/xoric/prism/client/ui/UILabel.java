package xoric.prism.client.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;

public class UILabel extends FloatRect implements IDrawableUI, IUITextComponent
{
	private static final float BORDER = 20.0f;
	private UITextArea textArea;

	public UILabel()
	{
		textArea = new UITextArea();
	}

	public void rearrangeText()
	{
		textArea.setX(topLeft.x + BORDER);
		textArea.setY(topLeft.y + BORDER);
		textArea.setWidth(size.x - 2.0f * BORDER);
		textArea.setHeight(size.y - 2.0f * BORDER);
	}

	public void setText(IText_r text)
	{
		textArea.setText(text);
		textArea.rearrangeLines();
	}

	@Override
	public void setText(Text text)
	{
		textArea.setText(text);
		textArea.rearrangeLines();
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		textArea.draw(renderer);
	}

	@Override
	public IText_r getText()
	{
		return textArea.getText();
	}
}
