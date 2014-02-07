package xoric.prism.client.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.client.ui.actions.ButtonAction;
import xoric.prism.client.ui.actions.ButtonActionIndex;
import xoric.prism.client.ui.actions.ButtonActionMask;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

public class UIButton extends UIComponent implements IUITextComponent
{
	private final IUIButtonHost inputHub;
	private final UITextLine textLine;

	private ButtonActionIndex actionIndex;
	private ButtonActionMask actionMask;

	public UIButton(IUIButtonHost inputHub)
	{
		this.inputHub = inputHub;
		this.actionIndex = ButtonActionIndex.NONE;

		registerChild(textLine = new UITextLine());

		//		actionMask = new ButtonActionMask();
		//		actionMask.getIntSources().add(0);
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
		ButtonAction a = new ButtonAction(actionIndex);

		if (actionMask != null)
			a.importData(inputHub, actionMask);

		inputHub.executeAction(a);
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

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		super.unpack(stream);

		// unpack text and actionIndex
		textLine.unpack(stream);
		actionIndex = ButtonActionIndex.valueOf(IntPacker.unpack_s(stream));

		// unpack actionMask
		int i = IntPacker.unpack_s(stream);
		if (i > 0)
			actionMask.unpack(stream);
		else
			actionMask = null;
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		super.pack(stream);

		// pack text and actionIndex
		textLine.pack(stream);
		IntPacker.pack_s(stream, actionIndex.ordinal());

		// pack actionMask
		IntPacker.pack_s(stream, actionMask == null ? 0 : 1);
		if (actionMask != null)
			actionMask.pack(stream);
	}
}
