package xoric.prism.creator.windows.scene;

import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.ui.UIComponent;

public class DeleteComponentAction extends SceneAction
{
	public DeleteComponentAction()
	{
		super("REMOVE COMPONENT");
	}

	@Override
	public boolean mouseMove(IFloatPoint_r mouseOnWindow)
	{
		return true;
	}

	@Override
	public boolean onMouseDown(IFloatPoint_r mouseOnScreen, IFloatPoint_r mouseOnWindow)
	{

		UIComponent c = model.getWindow().findComponent(mouseOnScreen);

		if (c != null)
			control.requestDeleteComponent(c);

		return true;
	}

	@Override
	public boolean onMouseUp(IFloatPoint_r mouseOnWindow)
	{
		return true;
	}
}
