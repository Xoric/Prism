package xoric.prism.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.tools.AllTools;

public class UITextLine implements IDrawableUI, IUITextComponent, IUISubcomponent, IStackable
{
	private static final Text defaultText = new Text("");

	private Text text;
	private float defaultHalfTextWidth;
	//	private float halfTextWidth;
	private float defaultHalfTextHeight;
	//	private float scale;
	//	private boolean useScale;
	private IFloatRect_r parentRect;
	private final FloatPoint textPosition;

	public UITextLine()
	{
		textPosition = new FloatPoint();
		text = defaultText;

		//		setFontScale(Printer.DEFAULT_SCALE);
		//		useScale = false;
	}

	@Override
	public void setText(Text text)
	{
		this.text = text;
		this.defaultHalfTextWidth = 0.5f * AllTools.printer.calcDefaultTextWidth(text);
		//		this.halfTextWidth = defaultHalfTextWidth;

		//		if (useScale)
		//			halfTextWidth *= scale;

		repositionText();
	}

	@Override
	public IText_r getText()
	{
		return text;
	}

	//	public void setFontScale(float scale)
	//	{
	//		this.scale = scale;
	//		this.halfTextHeight = 0.5f * Materials.printer.getHeight(scale);
	//		this.halfTextWidth = defaultHalfTextWidth * scale;
	//		this.useScale = true;
	//
	//		repositionText();
	//	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		if (text != null)
		{
			AllTools.printer.setText(text);

			//			if (useScale)
			//				Materials.printer.print(textPosition, scale);
			//			else
			AllTools.printer.print(textPosition);
		}
	}

	@Override
	public void moveBy(float dx, float dy)
	{
		textPosition.x += dx;
		textPosition.y += dy;
	}

	private void repositionText()
	{
		if (parentRect != null)
		{
			textPosition.x = parentRect.getX() + parentRect.getWidth() * 0.5f - defaultHalfTextWidth;
			textPosition.y = parentRect.getY() + 7.0f;
		}
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		this.parentRect = parentRect;
		repositionText();
	}

	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		text = TextPacker.unpack_s(stream);
	//	}
	//
	//	@Override
	//	public void pack(OutputStream stream) throws IOException, PrismException
	//	{
	//		TextPacker.pack_s(stream, text);
	//	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.texts.add(text);
	}

	@Override
	public void extractFrom(Heap_in h)
	{
		setText(h.nextText());
	}
}
