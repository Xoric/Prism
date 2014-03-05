package xoric.prism.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.renderer.IUIRenderer2;

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
	public void draw(IUIRenderer2 ren) throws PrismException
	{
		textArea.draw(ren);
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

	//	@Override
	//	public void pack(OutputStream stream) throws IOException, PrismException
	//	{
	//		super.pack(stream);
	//
	//		TextPacker.pack_s(stream, textArea.getText());
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		super.unpack(stream);
	//
	//		textArea.setText(TextPacker.unpack_s(stream));
	//	}

	@Override
	public void appendTo(Heap_out h)
	{
		super.appendTo(h);

		// text
		h.texts.add(textArea.getText());
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		super.extractFrom(h);

		// text
		textArea.setText(h.nextText());
	}

	@Override
	public void onControlKey(int keyCode, boolean isDown)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onCharacterKey(char c, boolean isDown)
	{
		// TODO Auto-generated method stub

	}
}
