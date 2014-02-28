package xoric.prism.creator.ground.view;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.creator.ground.control.ISceneControl;
import xoric.prism.creator.ground.model.AllDrawableGrounds;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.PrismColor;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.camera.CameraOld;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.ui.BlinkColor;
import xoric.prism.world.client.map2.DrawableGround2;
import xoric.prism.world.map2.Ground2;

/**
 * @author XoricLee
 * @since 22.02.2014, 20:18:13
 */
public class SceneHandler extends Thread implements ISceneListener // ,IActionExecuter,IUIButtonHost,IExceptionSink
{
	private ISceneControl control;

	//	private Exception exception;

	private final FloatPoint textPos;
	private final Text selectedGroundText;
	private final List<Text> textLines;

	private int weatherTarget;

	private final BlinkColor blinkColor = new BlinkColor(new PrismColor(255, 0, 0, 50), new PrismColor(255, 0, 0, 200), 300);

	//	private final PrismUI ui;
	//	private UIWindow window;
	private float targetY;
	//	private UILabel label;
	//	private float labelWidth;
	private final FloatPoint perspectiveMouse;
	private final FloatPoint worldMouse;

	private FloatPoint tempPoint;
	//	private final FloatPoint tempMouse;

	private FloatRect screenRect;

	private final IScene scene;
	private final CameraOld camera;

	private FloatRect tempRect;

	private volatile int selectedIndex;

	public SceneHandler(IScene scene)
	{
		this.scene = scene;
		this.camera = new CameraOld(0, 0, 0, 0);

		this.screenRect = new FloatRect();

		selectedIndex = -1;

		tempRect = new FloatRect();

		weatherTarget = 200;

		textPos = new FloatPoint(10.0f, 10.0f);
		textLines = new ArrayList<Text>();
		textLines.add(selectedGroundText = new Text());
		textLines.add(new Text("USE THE ARROW KEYS TO SCROLL UP AND DOWN"));
		textLines.add(new Text("S: SNOW, R: RAIN, N: NORMAL, B: BURN"));

		perspectiveMouse = new FloatPoint();
		worldMouse = new FloatPoint();

		tempPoint = new FloatPoint();

		targetY = -50.0f;

		setSelectedGround(-1);
	}

	public void setControl(ISceneControl control)
	{
		this.control = control;
	}

	@Override
	public void run()
	{
		try
		{
			IFloatPoint_r size = scene.createWindow(700, 480, false);
			screenRect.setSize(size);
			camera.set(-20.0f, -50.0f, Ground2.WIDTH * 3 + 40.0f, Ground2.HEIGHT * 3 + 100.0f);
			scene.initialize();
			scene.startLoop(this, this);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	@Override
	public void onMouseMove(IFloatPoint_r mouse)
	{
	}

	@Override
	public boolean onMouseDown(IFloatPoint_r mouse, boolean isLeft)
	{
		perspectiveMouse.copyFrom(mouse);
		scene.applyPerspective(perspectiveMouse);
		camera.transformFrameFractionToWorld(perspectiveMouse, worldMouse);

		int x = (int) worldMouse.x;
		int y = (int) worldMouse.y;
		x = x < 0 ? -1 : x / Ground2.WIDTH;
		y = y < 0 ? -1 : y / Ground2.HEIGHT;
		int newIndex = -1;

		if (x >= 0 && x <= 2 && y >= 0)
		{
			int index = y * 3 + x;
			if (index >= 0 && index < AllDrawableGrounds.getCount())
				newIndex = index;
		}
		control.requestSelectGround(newIndex);

		return true;
	}

	public void setSelectedGround(int index)
	{
		selectedIndex = index;
		selectedGroundText.set("SELECTED GROUND: " + (selectedIndex >= 0 ? String.valueOf(selectedIndex) : "NONE"));
	}

	@Override
	public void onMouseUp(IFloatPoint_r mouse, boolean isLeft)
	{
	}

	@Override
	public boolean onControlKey(int keyCode, boolean isDown)
	{
		if (isDown)
		{
			if (keyCode == KeyEvent.VK_DOWN)
				targetY += Ground2.HEIGHT * 3;
			else if (keyCode == KeyEvent.VK_UP)
				targetY -= Ground2.HEIGHT * 3;
		}
		return true;
	}

	@Override
	public boolean onCharacterKey(char c, boolean isDown)
	{
		if (c == 'S')
			weatherTarget = 0;
		else if (c == 'R')
			weatherTarget = 100;
		else if (c == 'N')
			weatherTarget = 200;
		else if (c == 'B')
			weatherTarget = 300;

		return true;
	}

	@Override
	public boolean update(int passedMs)
	{
		blinkColor.update(passedMs);

		for (int i = 0; i < AllDrawableGrounds.getCount(); ++i)
		{
			DrawableGround2 g = AllDrawableGrounds.get(i);
			g.update(passedMs);

			int w = g.getWeatherFader().getWeather();
			if (w < weatherTarget)
				g.getWeatherFader().setWeather(w + 1);
			else if (w > weatherTarget)
				g.getWeatherFader().setWeather(w - 1);
		}

		final float step = 15.0f;

		if (camera.getY() < targetY - step)
			camera.addY(step);
		else if (camera.getY() > targetY + step)
			camera.addY(-step);

		return true;
	}

	@Override
	public void drawWorld(IRendererWorld renderer) throws PrismException
	{
		for (int i = 0; i < AllDrawableGrounds.getCount(); ++i)
		{
			DrawableGround2 g = AllDrawableGrounds.get(i);
			g.draw(renderer, camera);

			if (i == selectedIndex)
			{
				AllShaders.color.setColor(blinkColor.getMixedColor());
				IFloatRect_r texRect = AllArt.env0.getMeta().getRect(g.getGroundType().getAnimationStart());
				camera.transformRect(g.getRect(), tempRect);
				renderer.drawPlane(texRect, tempRect, 0.0f);
			}
		}
	}

	@Override
	public void drawUI(IRendererUI renderer) throws PrismException
	{
		tempPoint.copyFrom(textPos);

		for (Text t : textLines)
		{
			AllTools.printer.setText(t);
			AllTools.printer.print(tempPoint);
			tempPoint.y += 20.0f;
		}
	}

	@Override
	public void onClosingScene(Exception e)
	{
		if (e instanceof PrismException)
		{
			((PrismException) e).code.print();
			((PrismException) e).user.showMessage();
		}
		else if (e != null)
		{
			e.printStackTrace();
		}
	}
}
