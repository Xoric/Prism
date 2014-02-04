package xoric.prism.client;

import java.net.Socket;

import xoric.prism.client.ui.UiFrame;
import xoric.prism.com.ClientLoginMessage;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.net.NetConstants;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.camera.Camera;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.shaders.AllShaders;
import xoric.prism.scene.textures.TextureInfo;
import xoric.prism.world.entities.Movable;
import xoric.prism.world.model.GridModelMeta;

public class PrismClient implements ISceneListener
{
	private IScene scene;

	private int ni = 0;
	private int nis = 0;

	private float increasing;

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

	private final FloatRect testRect = new FloatRect(20.0f, 20.0f, 120.0f, 80.0f);
	private final FloatRect testRect2 = new FloatRect(20.0f, 220.0f, 120.0f, 80.0f);

	private final Text testText = new Text("§40 = $2?");
	private final FloatPoint testPosition = new FloatPoint(100.0f, 150.0f);

	private float fontScale;
	private boolean fontScalingUp;

	private final UiFrame uiFrame = new UiFrame();

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

		uiFrame.set(120.0f, 120.0f, 90.0f, 70.0f);
		uiFrame.setTitle(new Text("MY TITLE"));
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

	//	private void testCollectionMeta()
	//	{
	//		try
	//		{
	//			MetaFile mf = Prism.global.loadMetaFile(FileTableDirectoryIndex.UI, UIIndex.FRAMES.ordinal());
	//			CollectionMeta m = new CollectionMeta();
	//			m.load(mf.getMetaList());
	//
	//			System.out.println("objects: " + m.getObjectCount());
	//
	//			for (int i = 0; i < m.getObjectCount(); ++i)
	//			{
	//				ObjectMeta om = m.getObject(i);
	//
	//				System.out.println("Object " + i + " (" + om.getName() + "): " + om.getInstanceCount() + " instance(s) with "
	//						+ om.getInstance(0).getRectCount() + " rect(s)");
	//			}
	//		}
	//		catch (PrismException e)
	//		{
	//			// TODO Auto-generated catch block
	//			e.code.print();
	//			e.user.showMessage();
	//		}
	//	}

	public void start()
	{
		testModelMeta();

		scene.createWindow(800, 480, false);
		scene.initialize();
		scene.startLoop(this);
	}

	@Override
	public void onClosingScene(Exception e0)
	{
		System.out.println("client is being notified that scene is closing");

		if (e0 != null)
		{
			if (e0 instanceof PrismException)
			{
				PrismException e = (PrismException) e0;
				e.code.print();
				e.user.showMessage();
			}
			else
			{
				e0.printStackTrace();
			}
		}
	}

	@Override
	public boolean drawWorld(int passedMs, IRendererWorld renderer) throws Exception
	{
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
		TextureInfo texInfo = Materials.frames.getTextureInfo(0, 0, 0, ni);
		//		temp.y += 0.1f;
		renderer.drawPlane(texInfo, new FloatRect(temp, temp2));

		// draw edges
		//		renderer.setColor(0.3f, 0.5f, 0.2f);
		cam.transformWithCameraBounds(manSize, temp2);
		for (int i = 0; i < 4; ++i)
		{
			cam.transformWithCameraBounds(testMan[i], temp);
			texInfo = Materials.frames.getTextureInfo(0, 0, 1, ni);
			renderer.drawObject(texInfo, temp, temp2, 0.0f);
		}

		// draw walking man
		//		renderer.setColor(0.3f, 0.2f, 0.8f);
		cam.transformWithCameraBounds(walkingMan, temp);
		texInfo = Materials.frames.getTextureInfo(0, 0, 2, ni);
		renderer.drawObject(texInfo, temp, temp2, 0.0f);

		// draw movable
		//		renderer.setColor(0.3f, 0.2f, 0.5f);
		cam.transformWithCameraBounds(movable.getPosition(), temp);
		texInfo = Materials.frames.getTextureInfo(0, 0, 3, ni);
		renderer.drawObject(texInfo, temp, temp2, 0.0f);

		nis += passedMs;
		if (nis > 1000)
		{
			nis = 0;
			if (++ni > 2)
				ni = 0;
		}

		return true;
	}

	@Override
	public boolean drawUI(int passedMs, IRendererUI renderer) throws Exception
	{
		Materials.framesDrawer.setup(0, 1, 0).drawNineParts(testRect2);

		// test art
		TextureInfo texInfo = Materials.frames.getTextureInfo(0, 0, 0, ni);

		AllShaders.defaultShader.activate();
		AllShaders.defaultShader.setTexture(texInfo.getTexture());

		//		testRect.addY(-0.1f);
		//		renderer.drawSprite(texInfo, testRect);

		increasing += 0.5f;
		testRect.addY(0.1f);
		Materials.framesDrawer.setup(0, 0, 0).drawThreeParts(testRect.getTopLeft(), 30.0f + increasing);

		if (fontScalingUp)
		{
			fontScale += 0.003f;
			if (fontScale > 1.5f)
				fontScalingUp = false;
		}
		else
		{
			fontScale -= 0.002f;
			if (fontScale <= 0.5f)
			{
				fontScale = 0.5f;
				fontScalingUp = true;
			}
		}
		Materials.printer.print(testPosition, testText, fontScale);

		uiFrame.draw(renderer);

		return true;
	}
}
