package xoric.prism.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.scene.renderer.IUIRenderer2;

public class UIFrame extends UIComponentH implements IUITextComponent
{
	protected UITextLine titleLine;

	public UIFrame(UIIdentifier id)
	{
		super(id);
	}

	public UIFrame()
	{
		super(UIIdentifier.FRAME);
	}

	@Override
	public void setText(Text title)
	{
		if (titleLine == null)
			registerChild(titleLine = new UITextLine());

		titleLine.setText(title);
		//		titleLine.rearrange(rect);
	}

	@Override
	public IText_r getText()
	{
		return titleLine == null ? null : titleLine.getText();
	}

	@Override
	public void draw(IUIRenderer2 ren) throws PrismException
	{
		AllTools.uiDrawer.setup(0, 1, 0);
		AllTools.uiDrawer.drawNineParts(rect);

		if (titleLine != null)
		{
			int i = isMouseDown ? 1 : 0;
			AllTools.uiDrawer.setup(2, i);
			AllTools.uiDrawer.drawThreeParts(rect.getTopLeft(), rect.getWidth());

			titleLine.draw(ren);
		}
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
	//		int i = titleLine == null ? 0 : 1;
	//		IntPacker.pack_s(stream, i);
	//		if (titleLine != null)
	//			titleLine.pack(stream);
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		super.unpack(stream);
	//
	//		int i = IntPacker.unpack_s(stream);
	//		if (i > 0)
	//			titleLine.unpack(stream);
	//		else
	//			titleLine = null;
	//	}

	@Override
	public void appendTo(Heap_out h)
	{
		super.appendTo(h);

		// title
		h.bools.add(titleLine != null);
		if (titleLine != null)
			h.texts.add(titleLine.getText());
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		super.extractFrom(h);

		// title
		boolean b = h.nextBool();
		if (b)
			setText(h.nextText());
		else
			titleLine = null;
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
