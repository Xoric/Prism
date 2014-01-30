package xoric.prism.client.main;

import java.net.Socket;

import xoric.prism.com.ClientLoginMessage;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.global.UIIndex;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.net.NetConstants;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRenderer;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.SceneStage;
import xoric.prism.scene.camera.Camera;
import xoric.prism.scene.textures.collections.CollectionMeta;
import xoric.prism.scene.textures.collections.ObjectMeta;
import xoric.prism.world.entities.Movable;
import xoric.prism.world.model.GridModelMeta;

public class PrismClient implements ISceneListener
{
	private IScene scene;

	private int ni = 0;

	private float slope = 0.1f;
	private boolean isSlopeGrowing = true;

	private FloatRect testPlane;
	private FloatPoint testMan[];
	private FloatPoint walkingMan;
	private Movable movable;
	private float walkingX;
	private float walkingY;
	private static final FloatPoint manSize = new FloatPoint(20.0f, 40.0f);

	private GridModelMeta gm;

	private FloatPoint temp;
	private FloatPoint temp2;
	private Camera cam;

	public PrismClient(IScene scene)
	{
		this.scene = scene;

		testPlane = new FloatRect(0.0f, 0.0f, 800.0f, 480.0f);

		testMan = new FloatPoint[4];
		testMan[0] = new FloatPoint(0.0f, 0.0f);
		testMan[1] = new FloatPoint(800.0f - manSize.getX(), 0.0f);
		testMan[2] = new FloatPoint(0.0f, 480.0f);
		testMan[3] = new FloatPoint(800.0f - manSize.getX(), 480.0f);

		walkingMan = new FloatPoint(400.0f, 300.0f);
		walkingX = 4.0f;
		walkingY = 1.0f;

		movable = new Movable();
		movable.setAngle(290);
		movable.setSpeed(40.0f);

		temp = new FloatPoint();
		temp2 = new FloatPoint();
		cam = new Camera(0.0f, 0.0f, 800.0f, 480.0f);
	}

	public void testConnect()
	{
		try
		{
			Socket socket = new Socket("127.0.0.1", NetConstants.port);

			Thread.sleep(1500);
			//			String s = "Can you read this?";
			//			socket.getOutputStream().write(s.getBytes());
			//
			//			Thread.sleep(200);
			//			s = "And this?";
			//			socket.getOutputStream().write(s.getBytes());
			//
			//			Thread.sleep(1000);
			//			s = "How about...";
			//			socket.getOutputStream().write(s.getBytes());
			//			s = "This...";
			//			socket.getOutputStream().write(s.getBytes());
			//			s = "That...";
			//			socket.getOutputStream().write(s.getBytes());
			//			s = "...and finally this?";
			//			socket.getOutputStream().write(s.getBytes());

			ClientLoginMessage m = new ClientLoginMessage();
			m.setPassword(new Text("JOHN'S PASSWORD!"));
			m.getHeap().ints.add(17);
			m.getHeap().texts.add(new Text("JOHN"));
			m.pack(socket.getOutputStream());

			//			socket.getOutputStream()

		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void testModelMeta()
	{
		try
		{
			MetaFile mf = new MetaFile(new Path("E:\\Prism\\data\\model\\creep"), "fennec.md");
			mf.load();
			MetaList metaList = mf.getMetaList();

			gm = new GridModelMeta();
			gm.load(metaList);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private void testCollectionMeta()
	{
		try
		{
			MetaFile mf = Prism.global.loadMetaFile(FileTableDirectoryIndex.UI, UIIndex.FRAMES.ordinal());
			CollectionMeta m = new CollectionMeta();
			m.load(mf.getMetaList());

			System.out.println("objects: " + m.getObjectCount());

			for (int i = 0; i < m.getObjectCount(); ++i)
			{
				ObjectMeta om = m.getObject(i);

				System.out.println("Object " + i + " (" + om.getName() + "): " + om.getInstanceCount() + " instance(s) with "
						+ om.getInstance(0).getRectCount() + " rect(s)");
			}
		}
		catch (PrismException e)
		{
			// TODO Auto-generated catch block
			e.code.print();
			e.user.showMessage();
		}
	}

	public void start()
	{
		testModelMeta();
		testCollectionMeta();

		scene.createWindow(800, 480, false);
		scene.initialize();
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

		float seconds = 0.001f * passedMs;
		movable.timeUpdate(passedMs, seconds);

		//		nz = -3.0f;
		//		renderer.setColor(0.8f, 0.2f, 0.2f);

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
		else if (walkingMan.y > 480.0f)
		{
			walkingY = -1.0f * (float) (1.0f + Math.random() * 4.0f);
		}

		// draw planes
		cam.transformWithCameraBounds(testPlane.getTopLeft(), temp);
		cam.transformWithCameraBounds(testPlane.getSize(), temp2);
		renderer.drawPlane(temp, temp2);

		// draw edges
		//		renderer.setColor(0.3f, 0.5f, 0.2f);
		cam.transformWithCameraBounds(manSize, temp2);
		for (int i = 0; i < 4; ++i)
		{
			cam.transformWithCameraBounds(testMan[i], temp);
			renderer.drawObject(temp, temp2, 0.0f);
		}

		// draw walking man
		//		renderer.setColor(0.3f, 0.2f, 0.8f);
		cam.transformWithCameraBounds(walkingMan, temp);
		renderer.drawObject(temp, temp2, 0.0f);

		// draw movable
		//		renderer.setColor(0.3f, 0.2f, 0.5f);
		cam.transformWithCameraBounds(movable.getPosition(), temp);
		renderer.drawObject(temp, temp2, 0.0f);

		return true;
	}

	@Override
	public void onClosingScene(Throwable throwable)
	{
		System.out.println("client is being notified that scene is closing");

		if (throwable != null)
		{
			if (throwable instanceof PrismException)
			{
				PrismException e = (PrismException) throwable;
				e.code.print();
				e.user.showMessage();
			}
			else
			{
				throwable.printStackTrace();
			}
		}
	}
}
