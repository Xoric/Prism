package xoric.prism.client.ui.button;

import xoric.prism.client.ui.IActiveUI;
import xoric.prism.client.ui.IUIButtonHost;
import xoric.prism.client.ui.IUITextComponent;
import xoric.prism.client.ui.UIComponent;
import xoric.prism.client.ui.UIIdentifier;
import xoric.prism.client.ui.UITextLine;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

public class UIButton extends UIComponent implements IUITextComponent
{
	private final IUIButtonHost buttonHost;
	private final UITextLine textLine;

	private ButtonActionIndex actionIndex;
	private ButtonActionMask actionMask;
	private boolean isClosingWindow;

	public UIButton(IUIButtonHost buttonHost)
	{
		super(UIIdentifier.BUTTON);

		this.buttonHost = buttonHost;
		this.actionIndex = ButtonActionIndex.NONE;

		registerChild(textLine = new UITextLine());

		//		actionMask = new ButtonActionMask();
		//		actionMask.getIntSources().add(0);
	}

	public ButtonActionMask getActionMask()
	{
		return actionMask;
	}

	public ButtonActionIndex getActionIndex()
	{
		return actionIndex;
	}

	public boolean isClosingWindow()
	{
		return isClosingWindow;
	}

	@Override
	public void setText(Text text)
	{
		textLine.setText(text);
	}

	@Override
	public IText_r getText()
	{
		return textLine.getText();
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		int i = isMouseDown ? 2 : 0;
		Materials.framesDrawer.setup(0, 0, i);
		Materials.framesDrawer.drawThreeParts(rect.getTopLeft(), rect.getWidth());

		textLine.draw(renderer);
	}

	@Override
	public void mouseClick() throws PrismException
	{
		ButtonAction a = new ButtonAction(actionIndex, isClosingWindow);

		if (actionMask != null)
			a.importData(buttonHost, actionMask);

		buttonHost.executeAction(a);
	}

	@Override
	protected IActiveUI mouseDownConfirmed(IFloatPoint_r mouse)
	{
		textLine.moveBy(0.0f, 1.0f);
		return this;
	}

	@Override
	protected void mouseUpConfirmed()
	{
		textLine.moveBy(0.0f, -1.0f);
	}

	public void setActionIndex(ButtonActionIndex actionIndex)
	{
		this.actionIndex = actionIndex;
	}

	//	@Override
	//	public void pack(OutputStream stream) throws IOException, PrismException
	//	{
	//		super.pack(stream);
	//
	//		// pack text and actionIndex
	//		textLine.pack(stream);
	//		IntPacker.pack_s(stream, actionIndex.ordinal());
	//
	//		// pack actionMask
	//		IntPacker.pack_s(stream, actionMask == null ? 0 : 1);
	//		if (actionMask != null)
	//			actionMask.pack(stream);
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		super.unpack(stream);
	//
	//		// unpack text and actionIndex
	//		textLine.unpack(stream);
	//		actionIndex = ButtonActionIndex.valueOf(IntPacker.unpack_s(stream));
	//
	//		// unpack actionMask
	//		int i = IntPacker.unpack_s(stream);
	//		if (i > 0)
	//			actionMask.unpack(stream);
	//		else
	//			actionMask = null;
	//	}

	@Override
	public void appendTo(Heap_out h)
	{
		super.appendTo(h);

		// text and actionIndex
		textLine.appendTo(h);
		h.ints.add(actionIndex.ordinal());

		// close window
		h.bools.add(isClosingWindow);

		// actionMask
		h.bools.add(actionMask != null);
		if (actionMask != null)
			actionMask.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		super.extractFrom(h);

		// text and actionIndex
		textLine.extractFrom(h);
		actionIndex = ButtonActionIndex.valueOf(h.nextInt());

		// close window
		isClosingWindow = h.nextBool();

		// actionMask
		boolean b = h.nextBool();
		if (b)
		{
			actionMask = new ButtonActionMask();
			actionMask.extractFrom(h);
		}
		else
			actionMask = null;
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

	public void setActionMask(ButtonActionMask m)
	{
		this.actionMask = m;
	}

	public void setClosingWindow(boolean b)
	{
		isClosingWindow = b;
	}
}
