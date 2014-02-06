package xoric.prism.creator.windows.control;

import xoric.prism.client.ui.UIWindow;
import xoric.prism.creator.windows.model.WindowModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.scene.IInputListener;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;

public class SceneHandler extends Thread implements ISceneListener, IInputListener
{
	private final IScene scene;
	private final IPoint_r resolution;
	private ISceneControl control;

	private WindowModel model;

	private volatile boolean isStopRequested;

	public SceneHandler(IScene scene, IPoint_r resolution)
	{
		this.scene = scene;
		this.resolution = resolution;
	}

	public void setModel(WindowModel model)
	{
		this.model = model;
	}

	public void requestStop()
	{
		isStopRequested = true;
	}

	@Override
	public void run()
	{
		try
		{
			scene.createWindow(resolution.getX(), resolution.getY(), false);
			scene.initialize();
			scene.startLoop(this, this);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	public void setControl(ISceneControl control)
	{
		this.control = control;
	}

	@Override
	public void mouseMove(IFloatPoint_r mouse)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseDown(IFloatPoint_r mouse, boolean isLeft)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseUp(IFloatPoint_r mouse, boolean isLeft)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean drawWorld(int passedMs, IRendererWorld renderer) throws Exception
	{
		return !isStopRequested;
	}

	@Override
	public boolean drawUI(int passedMs, IRendererUI renderer) throws Exception
	{
		if (model != null)
		{
			UIWindow w = model.getWindow();
			if (w != null)
				w.draw(renderer);
		}
		return !isStopRequested;
	}

	@Override
	public void onClosingScene(Exception e)
	{
		if (e != null)
			e.printStackTrace();
	}
}
