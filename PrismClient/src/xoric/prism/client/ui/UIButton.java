package xoric.prism.client.ui;

import xoric.prism.client.ui.actions.ButtonAction;
import xoric.prism.client.ui.actions.ButtonActionIndex;
import xoric.prism.client.ui.actions.ButtonActionMask;
import xoric.prism.data.exceptions.PrismException;
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
}
