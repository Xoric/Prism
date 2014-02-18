package xoric.prism.creator.windows.scene;

import xoric.prism.creator.windows.control.ISceneControl;
import xoric.prism.creator.windows.model.WindowModel;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;

public abstract class SceneAction
{
	protected ISceneControl control;
	protected FloatRect sceneRect;
	protected WindowModel model;
	protected Align align;
	private Text title;

	public SceneAction(String title)
	{
		this.title = new Text(title);
	}

	public IText_r getTitle()
	{
		return title;
	}

	public void setModel(WindowModel model)
	{
		this.model = model;
	}

	public void setControl(ISceneControl control)
	{
		this.control = control;
	}

	public abstract boolean mouseMove(IFloatPoint_r mouseOnWindow);

	public abstract boolean onMouseDown(IFloatPoint_r mouseOnScreen, IFloatPoint_r mouseOnWindow);

	public abstract boolean onMouseUp(IFloatPoint_r mouseOnWindow);

	public void setScreenSize(FloatRect sceneRect)
	{
		this.sceneRect = sceneRect;
	}

	public void setAlign(Align align)
	{
		this.align = align;
	}
}
