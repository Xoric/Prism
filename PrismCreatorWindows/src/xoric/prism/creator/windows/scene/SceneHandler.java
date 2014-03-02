package xoric.prism.creator.windows.scene;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.windows.control.ISceneControl;
import xoric.prism.creator.windows.model.WindowModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IInputListener;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.ui.UIWindow;

public class SceneHandler extends Thread implements ISceneListener, IInputListener
{
	private static final IFloatPoint_r actionPos = new FloatPoint(20.0f, 20.0f);

	private final IScene scene;
	private final IPoint_r resolution;
	private final FloatRect sceneRect;
	private ISceneControl control;
	private SceneAction action;

	private WindowModel model;
	private final List<Hint> hints;
	private Align align;

	private volatile boolean isStopRequested;

	public SceneHandler(IScene scene, IPoint_r resolution)
	{
		this.scene = scene;
		this.resolution = resolution;
		this.sceneRect = new FloatRect(0.0f, 0.0f, resolution.getX(), resolution.getY());
		this.hints = new ArrayList<Hint>();
		this.align = Align.LEFT;
	}

	public void setAction(SceneAction action)
	{
		this.action = action;
		action.setControl(control);
		action.setScreenSize(sceneRect);
		action.setAlign(align);
		action.setModel(model);
	}

	public void setModel(WindowModel model)
	{
		this.model = model;

		if (action != null)
			action.setModel(model);
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

	private FloatPoint calcMouseOnWindow(IFloatPoint_r mouse)
	{
		FloatPoint m = new FloatPoint(mouse);
		IFloatRect_r rect = model.getWindow().getCurrentRect();
		m.subtract(rect.getTopLeft());
		return m;
	}

	private void checkAlignment(FloatPoint mouseOnScreen)
	{
		//		m.divide(model.getWindow().getCurrentSize());
		//		FloatPoint m = calcMouseOnWindow(mouse);
		IFloatRect_r rect = model.getWindow().getCurrentRect();

		if (mouseOnScreen.x < 50.0f)
		{
			FloatPoint p = new FloatPoint(rect.getX() - 40.0f, rect.getY() + rect.getHeight() * 0.5f);
			Hint h = new Hint(new Text("ALIGN: LEFT"), p);
			hints.add(h);
			align = Align.LEFT;
		}
		else if (mouseOnScreen.x > rect.getWidth() - 50.0f)
		{
			FloatPoint p = new FloatPoint(rect.getRight() - 60.0f, rect.getY() + rect.getHeight() * 0.5f);
			Hint h = new Hint(new Text("ALIGN: RIGHT"), p);
			hints.add(h);
			align = Align.RIGHT;
		}
		else if (mouseOnScreen.x > rect.getWidth() * 0.5f - 30.0f && mouseOnScreen.x < rect.getWidth() * 0.5f + 30.0f)
		{
			FloatPoint p = new FloatPoint(rect.getX() + rect.getWidth() * 0.5f - 60.0f, rect.getY() + rect.getHeight() * 0.5f);
			Hint h = new Hint(new Text("ALIGN: CENTER"), p);
			hints.add(h);
			align = Align.CENTER;
		}

		if (action != null)
			action.setAlign(align);
	}

	@Override
	public void onMouseMove(IFloatPoint_r mouse)
	{
		if (action != null)
		{
			FloatPoint m = calcMouseOnWindow(mouse);

			if (!action.mouseMove(m))
				action = null;
		}
	}

	@Override
	public boolean onMouseDown(IFloatPoint_r mouse, boolean isLeft)
	{
		FloatPoint m = calcMouseOnWindow(mouse);

		if (!isLeft)
		{
			checkAlignment(m);
		}
		else
		{
			if (action != null)
				if (!action.onMouseDown(mouse, m))
					action = null;
		}
		return true;
	}

	@Override
	public void onMouseUp(IFloatPoint_r mouse, boolean isLeft)
	{
		if (action != null && !isLeft)
		{
			FloatPoint m = calcMouseOnWindow(mouse);

			if (!action.onMouseUp(m))
				action = null;
		}
	}

	@Override
	public void onClosingScene(Exception e)
	{
		if (e != null)
			e.printStackTrace();
	}

	@Override
	public boolean onControlKey(int keyCode, boolean isDown)
	{
		return true;
	}

	@Override
	public boolean onCharacterKey(char c, boolean isDown)
	{
		return true;
	}

	@Override
	public boolean update(int passedMs)
	{
		for (int i = hints.size() - 1; i >= 0; --i)
		{
			Hint h = hints.get(i);
			boolean b = h.update(passedMs);
			if (!b)
				hints.remove(i);
		}

		return !isStopRequested;
	}

	@Override
	public void drawWorld(IRendererWorld renderer)
	{
	}

	@Override
	public void drawUI(IRendererUI renderer) throws PrismException
	{
		if (model != null)
		{
			UIWindow w = model.getWindow();
			if (w != null)
				w.draw(renderer);

			if (action != null)
			{
				AllTools.printer.setText(action.getTitle());
				AllTools.printer.print(actionPos);
			}

			for (Hint h : hints)
				h.draw(renderer);
		}
	}
}
