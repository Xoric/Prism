package xoric.prism.client.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.TextPacker;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IDrawableUI;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.materials.Printer;

public class UITextLine implements IDrawableUI, IUITextComponent, IUIChild, IPackable
{
	private static final Text DEFAULT_TEXT = new Text("");

	private Text text;
	private float defaultHalfTextWidth;
	private float halfTextWidth;
	private float halfTextHeight;
	private float scale;
	private boolean useScale;
	private IFloatRect_r parentRect;
	private final FloatPoint textPosition;

	public UITextLine()
	{
		textPosition = new FloatPoint();
		text = DEFAULT_TEXT;

		setFontScale(Printer.DEFAULT_SCALE);
		useScale = false;
	}

	@Override
	public void setText(Text text)
	{
		this.text = text;
		this.defaultHalfTextWidth = 0.5f * Materials.printer.calcTextWidth(text);
		this.halfTextWidth = defaultHalfTextWidth * scale;

		repositionText();
	}

	@Override
	public IText_r getText()
	{
		return text;
	}

	public void setFontScale(float scale)
	{
		this.scale = scale;
		this.halfTextHeight = 0.5f * Materials.printer.getHeight(scale);
		this.halfTextWidth = defaultHalfTextWidth * scale;
		this.useScale = true;

		repositionText();
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		if (text != null)
		{
			Materials.printer.setText(text);

			if (useScale)
				Materials.printer.print(textPosition, scale);
			else
				Materials.printer.print(textPosition);
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
			textPosition.x = parentRect.getX() + parentRect.getWidth() * 0.5f - halfTextWidth;
			textPosition.y = parentRect.getY() + halfTextHeight;
		}
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		this.parentRect = parentRect;
		repositionText();
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		text = TextPacker.unpack_s(stream);
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		TextPacker.pack_s(stream, text);
	}
}
