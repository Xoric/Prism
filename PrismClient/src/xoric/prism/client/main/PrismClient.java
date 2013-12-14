package xoric.prism.client.main;

import xoric.prism.data.FloatPoint;
import xoric.prism.data.FloatRect;
import xoric.prism.scene.IRenderer;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.SceneStage;
import xoric.prism.scene.camera.Camera;

public class PrismClient implements ISceneListener
{
	private IScene scene;

	private float nx = 0.0f;
	private float ny = 0.0f;
	private float nz = 0.0f;
	private int ni = 0;

	private float slope = 0.1f;
	private boolean isSlopeGrowing = true;

	private FloatRect testPlane;
	private FloatPoint testMan[];
	private FloatPoint walkingMan;
	private float walkingX;
	private float walkingY;
	private static final FloatPoint manSize = new FloatPoint(20.0f, 40.0f);

	private FloatPoint temp;
	private FloatPoint temp2;
	private Camera cam;

	public PrismClient(IScene scene)
	{
		this.scene = scene;

		testPlane = new FloatRect(0.0f, 0.0f, 800.0f, 600.0f);

		testMan = new FloatPoint[4];
		testMan[0] = new FloatPoint(0.0f, 0.0f);
		testMan[1] = new FloatPoint(800.0f - manSize.getX(), 0.0f);
		testMan[2] = new FloatPoint(0.0f, 600.0f);
		testMan[3] = new FloatPoint(800.0f - manSize.getX(), 600.0f);

		walkingMan = new FloatPoint(400.0f, 300.0f);
		walkingX = 4.0f;
		walkingY = 1.0f;

		temp = new FloatPoint();
		temp2 = new FloatPoint();
		cam = new Camera(0.0f, 0.0f, 800.0f, 600.0f);
	}

	public void start()
	{
		scene.createWindow(800, 600, false);
		scene.startLoop(this);
	}

	@Override
	public boolean requestUpdateScene(int passedMs, IRenderer renderer)
	{
		if (ni == 0)
		{
			ni = 1;
			scene.setStage(SceneStage.GROUND);
		}

		if (isSlopeGrowing)
		{
			slope += 0.01f;
			if (slope > 4.4f)
			{
				slope = 4.5f;
				isSlopeGrowing = false;
			}
		}
		else
		{
			slope -= 0.01f;
			if (slope < 0.0f)
			{
				slope = 0.0f;
				isSlopeGrowing = true;
			}
		}
		scene.setSlope(slope);

		//		nz = -3.0f;
		renderer.setColor(0.8f, 0.2f, 0.2f);

		// walk
		walkingMan.x += walkingX;
		walkingMan.y += walkingY;

		if (walkingMan.x < 0.0f)
		{
			walkingX = (float) (1.0f + Math.random() * 4.0f);
		}
		else if (walkingMan.x > 800.0f - manSize.getX())
		{
			walkingX = -1.0f * (float) (1.0f + Math.random() * 4.0f);
		}

		if (walkingMan.y < 0.0f)
		{
			walkingY = (float) (1.0f + Math.random() * 4.0f);
		}
		else if (walkingMan.y > 600.0f)
		{
			walkingY = -1.0f * (float) (1.0f + Math.random() * 4.0f);
		}

		// draw planes
		cam.transformWithCameraBounds(testPlane.getTopLeft(), temp);
		cam.transformWithCameraBounds(testPlane.getSize(), temp2);
		renderer.drawPlane(temp, temp2);

		// draw edges
		renderer.setColor(0.3f, 0.5f, 0.2f);
		cam.transformWithCameraBounds(manSize, temp2);
		for (int i = 0; i < 4; ++i)
		{
			cam.transformWithCameraBounds(testMan[i], temp);
			renderer.drawObject(temp, temp2, 0.0f);
		}

		// draw walking man
		renderer.setColor(0.3f, 0.2f, 0.8f);
		cam.transformWithCameraBounds(walkingMan, temp);
		renderer.drawObject(temp, temp2, 0.0f);

		//		renderer.setColor(0.2f, 0.2f, 0.2f);
		//		for (int i = 0; i 4 ; ++i)
		//			renderer.drawObject(testMan[i].getTopLeft(), size, dz);
		//		float w = 0.03f;
		//		float h = 0.1f;
		//		renderer.drawQuadWorld(-0.5f, -0.5f, w, h, -1.5f, -1.5f);
		//		renderer.drawQuadWorld(0.5f - w, -0.5f, w, h, -1.5f, -1.5f);
		//		renderer.drawQuadWorld(-0.5f, 0.5f, w, h, nz, nz);
		//		renderer.drawQuadWorld(0.5f - w, 0.5f, w, h, nz, nz);

		return true;
	}

	@Override
	public void onClosingScene()
	{
		System.out.println("client is being notified that scene is closing");

	}
}