package xoric.prism.client.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;

public class UILabel extends UIComponent implements IDrawableUI, IUITextComponent
{
	private final UITextArea textArea;

	public UILabel()
	{
		super(UIIdentifier.LABEL);

		textArea = new UITextArea();

		super.registerChild(textArea);
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

	@Override
	public void mouseClick() throws PrismException
	{
	}

	@Override
	protected IActiveUI mouseDownConfirmed(IFloatPoint_r mouse)
	{
		return null;
	}

	@Override
	protected void mouseUpConfirmed()
	{
	}
}
