package xoric.prism.client;

import java.net.Socket;

import xoric.prism.client.ui.PrismUI;
import xoric.prism.client.ui.UIWindow;
import xoric.prism.client.ui.actions.ButtonAction;
import xoric.prism.client.ui.actions.ButtonActionIndex;
import xoric.prism.client.ui.actions.IActionHandler;
import xoric.prism.com.ClientLoginMessage;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.net.NetConstants;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.IRendererWorld;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.camera.Camera;
import xoric.prism.scene.materials.Materials;
import xoric.prism.scene.textures.TextureInfo;
import xoric.prism.world.entities.Movable;
import xoric.prism.world.model.GridModelMeta;

public class PrismClient implements ISceneListener, IActionHandler
{
	private final IScene scene;
	private final PrismUI ui;
	private volatile Exception clientException;

	private int ni = 0;
	private int nis = 0;

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
		this.ui = new PrismUI(this);

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

		//		ta.test();
	}

	public void testConnect()
	{
		try
		{
			Socket socket = new Socket("127.0.0.1", NetConstants.port);

			Thread.sleep(1500);

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

	public void start() throws PrismException
	{
		testModelMeta();

		IFloatPoint_r screenSize = scene.createWindow(800, 480, false);
		ui.setScreenSize(screenSize);

		for (int i = 0; i < 10; ++i)
		{
			UIWindow uiWindow = new UIWindow(null);
			uiWindow.setXRuler(20.0f + i * 20, 0.0f);
			uiWindow.setYRuler(40.0f + i * 5, 0.0f);
			uiWindow.setWidthRuler(150.0f, 0.0f);
			uiWindow.setHeightRuler(90.0f, 0.0f);
			uiWindow.setText(new Text("WINDOW" + i));

			ui.addWindow(uiWindow);
		}

		scene.initialize();
		scene.startLoop(this, this);
	}

	@Override
	public void onClosingScene(Exception sceneException)
	{
		System.out.println("client is being notified that scene is closing");

		if (clientException != null)
		{
			System.out.println("client exception:");

			if (clientException instanceof PrismException)
			{
				PrismException e = (PrismException) clientException;
				e.code.print();
				e.user.showMessage();
			}
			else
			{
				clientException.printStackTrace();
			}
		}
		else if (sceneException != null)
		{
			System.out.println("scene exception:");

			if (sceneException instanceof PrismException)
			{
				PrismException e = (PrismException) sceneException;
				e.code.print();
				e.user.showMessage();
			}
			else
			{
				sceneException.printStackTrace();
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

		return clientException == null;
	}

	@Override
	public boolean drawUI(int passedMs, IRendererUI renderer) throws Exception
	{

		// test art

		//		testRect.addY(-0.1f);
		//		renderer.drawSprite(texInfo, testRect);

		//		Materials.printer.setText(testText);
		//		Materials.printer.print(testPosition, fontScale);

		//		uiWindow.setWidth(uiWindow.getWidth() + 0.06f);
		//		uiWindow.setHeight(uiWindow.getHeight() + 0.02f);
		//		uiWindow.draw(renderer);

		//		ta.draw(renderer);

		ui.draw(renderer);

		return clientException == null;
	}

	@Override
	public void mouseMove(IFloatPoint_r mouse)
	{
		ui.mouseMove(mouse);
	}

	@Override
	public void onMouseDown(IFloatPoint_r mouse, boolean isLeft)
	{
		scene.setWindowTitle(mouse.toString());

		if (isLeft)
			ui.mouseDown(mouse);
	}

	@Override
	public void onMouseUp(IFloatPoint_r mouse, boolean isLeft)
	{
		if (isLeft)
		{
			try
			{
				ui.mouseUp(mouse);
			}
			catch (PrismException e)
			{
				if (clientException == null)
					clientException = e;
			}
		}
	}

	@Override
	public void executeAction(UIWindow w, ButtonAction a)
	{
		if (a.getActionIndex() == ButtonActionIndex.CLOSE_WINDOW)
			ui.closeWindow(w);
	}
}
