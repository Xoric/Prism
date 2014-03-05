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
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.art.ITextureBinder;
import xoric.prism.scene.camera.CameraOld;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.scene.renderer.IUIRenderer2;
import xoric.prism.scene.renderer.IWorldRenderer2;
import xoric.prism.scene.shaders.IShaderIO;
import xoric.prism.ui.BlinkColor;
import xoric.prism.world.client.map2.DrawableGround2;
import xoric.prism.world.map.AllGrounds;
import xoric.prism.world.map2.Ground2;

/**
 * @author XoricLee
 * @since 22.02.2014, 20:18:13
 */
public class SceneHandler extends Thread implements ISceneListener // ,IActionExecuter,IUIButtonHost,IExceptionSink
{
	public static final int columns = 5;

	private ISceneControl control;

	//	private Exception exception;

	private final FloatPoint textPos;
	private final Text groundCountText;
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

	private int lastGroundCount;

	private FloatRect screenRect;

	private final IScene scene;
	private final CameraOld camera;

	private volatile int selectedIndex;

	private final IUIRenderer2 renderer;
	private final ITextureBinder textureBinder;
	private final IShaderIO shaderIO;

	public SceneHandler(IScene scene, IUIRenderer2 ren, ITextureBinder textureBinder, IShaderIO shaderIO)
	{
		this.scene = scene;
		this.camera = new CameraOld(0, 0, 0, 0);

		this.screenRect = new FloatRect();

		selectedIndex = -1;

		weatherTarget = 200;

		textPos = new FloatPoint(10.0f, 10.0f);
		textLines = new ArrayList<Text>();
		textLines.add(groundCountText = new Text());
		textLines.add(selectedGroundText = new Text());
		textLines.add(new Text("USE THE ARROW KEYS TO SCROLL UP AND DOWN"));
		textLines.add(new Text("S: SNOW, R: RAIN, N: NORMAL, B: BURN"));

		lastGroundCount = -1;
		updateGroundCount();

		perspectiveMouse = new FloatPoint();
		worldMouse = new FloatPoint();

		tempPoint = new FloatPoint();

		targetY = -50.0f;

		this.renderer = ren;
		this.textureBinder = textureBinder;
		this.shaderIO = shaderIO;

		setSelectedGround(-1);
	}

	public void setControl(ISceneControl control)
	{
		this.control = control;
	}

	private void updateGroundCount()
	{
		int n = AllGrounds.list.size();
		if (lastGroundCount != n)
		{
			groundCountText.set("GROUND COUNT: " + n);
			lastGroundCount = n;
		}
	}

	@Override
	public void run()
	{
		try
		{
			IFloatPoint_r size = scene.createWindow(700, 480, false);
			screenRect.setSize(size);
			camera.set(-20.0f, -50.0f, Ground2.WIDTH * columns + 40.0f, Ground2.HEIGHT * columns + 100.0f);
			scene.initialize();

			// load materials
			Materials.loadAll(renderer, textureBinder, shaderIO);
			AllDrawableGrounds.loadAll(columns);

			scene.startLoop(this, this);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

		if (x >= 0 && x < columns && y >= 0)
		{
			int index = y * columns + x;
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

		updateGroundCount();

		return true;
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

	@Override
	public void drawWorld(IWorldRenderer2 ren) throws PrismException
	{
		for (int i = 0; i < AllDrawableGrounds.getCount(); ++i)
		{
			DrawableGround2 g = AllDrawableGrounds.get(i);
			g.draw(ren);

			if (i == selectedIndex)
			{
				AllShaders.color.activate();
				AllShaders.color.setColor(blinkColor.getMixedColor());
				IFloatRect_r texRect = AllArt.env0.getMeta().getRect(g.getGroundType().getAnimationStart());
				ren.reset();
				ren.setTexInfo(0, texRect);
				ren.setSprite(g.getRect());
				ren.drawPlane(1);
			}
		}
	}

	@Override
	public void drawUI(IUIRenderer2 ren) throws PrismException
	{
		tempPoint.copyFrom(textPos);

		for (Text t : textLines)
		{
			AllTools.printer.setText(t);
			AllTools.printer.print(tempPoint);
			tempPoint.y += 20.0f;
		}
	}
}
