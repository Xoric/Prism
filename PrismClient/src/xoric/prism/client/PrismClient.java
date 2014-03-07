package xoric.prism.client;

import java.awt.event.KeyEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.client.control.INetworkControl;
import xoric.prism.client.net.Network;
import xoric.prism.client.ui.ActionExecuter;
import xoric.prism.client.ui.ConnectionHint;
import xoric.prism.com.ClientLoginMessage_out;
import xoric.prism.data.exceptions.IExceptionSink;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.net.NetConstants;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.mouse.MouseButton;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.camera.Camera;
import xoric.prism.scene.camera.CameraOld;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.scene.materials.shaders.AllShaders;
import xoric.prism.scene.materials.tools.AllTools;
import xoric.prism.scene.renderer.IUIRenderer2;
import xoric.prism.scene.renderer.IWorldRenderer2;
import xoric.prism.ui.BlinkColor;
import xoric.prism.ui.PrismUI;
import xoric.prism.ui.UIWindow;
import xoric.prism.ui.button.IActionParent;
import xoric.prism.ui.edit.UIEdit;
import xoric.prism.world.client.map2.DrawableGround2;
import xoric.prism.world.client.map2.Mushroom;
import xoric.prism.world.client.map2.WeatherMark;
import xoric.prism.world.entities.Movable;
import xoric.prism.world.map.AllGrounds;
import xoric.prism.world.map2.GroundType2;
import xoric.prism.world.model.GridModelMeta;

public class PrismClient implements ISceneListener, IActionParent, INetworkControl, IExceptionSink
{
	private final IFloatPoint_r mouseOnScreen;
	private final IFloatPoint_r mouseInWorld;

	private final ConnectionHint connectionHint;

	private final Text mouseText1;
	private final Text mouseText2;
	private final FloatPoint mouseTextPos1;
	private final FloatPoint mouseTextPos2;

	private final IScene scene;
	private IFloatPoint_r frameSize;
	private final PrismUI ui;
	private final ActionExecuter actionExecuter;
	private final Network network;
	private volatile Exception clientException;
	private boolean exitGameRequested;

	private boolean isDebugDecoActive;

	private int ni = 0;
	private int nis = 0;

	private float slope;
	private boolean isSlopeGrowing = true;
	private boolean isSlopeFixed = true;

	private final FloatPoint target;

	private FloatRect testMouse;
	private FloatRect camZeroRect;

	private FloatRect testRectZ1;
	private FloatRect testRectZ2;
	private FloatRect testRectZ3;

	private final FloatPoint perspectiveMouse;
	private final FloatPoint worldMouse;

	private FloatRect testPlane;
	private FloatRect testPlane2;
	private FloatRect testSprite;
	private FloatRect testSpriteTemp;

	private IFloatPoint_r mouseRef;

	private FloatPoint testMan[];
	private FloatPoint walkingMan;
	private Movable movable;
	private float walkingX;
	private float walkingY;
	private static final FloatPoint manSize = new FloatPoint(20.0f, 40.0f);

	//	private FloatRect testGround = new FloatRect(40.0f, 40.0f, 160.0f, 120.0f);
	private FloatRect testGround = new FloatRect(40.0f, 40.0f, 160.0f, 120.0f);

	private GridModelMeta gm;

	private FloatPoint temp;
	private FloatPoint temp2;
	private final Camera cam;

	private FloatRect tempRect;
	//	private FloatRect tempMushroom;
	//	private FloatRect tempMushroom2;
	//	private PrismColor mushroomColor;
	//	private float mushroomState = 0.5f;
	//	private boolean isMushroomGrowing = true;
	private Text mushroomText;
	private FloatPoint mushroomTextPos;

	private FloatRect testRect;

	private List<Mushroom> mush;

	private Text testText = new Text();

	private DrawableGround2[][] testTile;
	private DrawableGround2[][] testSubTile;

	private WeatherMark tempMark;

	public PrismClient(IScene scene, Camera cam)
	{
		this.mouseOnScreen = scene.getMouseOnScreen();
		this.mouseInWorld = scene.getMouseInWorld();
		this.cam = cam;

		this.connectionHint = new ConnectionHint();

		mouseText1 = new Text();
		mouseText2 = new Text();
		mouseTextPos1 = new FloatPoint(25.0f, 350.0f);
		mouseTextPos2 = new FloatPoint(25.0f, 375.0f);

		this.scene = scene;
		this.network = new Network(this);
		this.actionExecuter = new ActionExecuter(this, network);
		this.ui = new PrismUI(this, actionExecuter, mouseOnScreen);

		//		testPlane = new FloatRect(200.0f, 100.0f, 120.0f, 80.0f);
		testPlane = new FloatRect(0.0f, 0.0f, 120.0f, 80.0f);
		testPlane2 = new FloatRect(0.0f, 0.0f, 120.0f, 80.0f);
		testSprite = new FloatRect(0.0f, 0.0f, 35.0f, 34.0f);
		testSpriteTemp = new FloatRect(0.0f, 0.0f, 35.0f, 34.0f);

		target = new FloatPoint();

		testMan = new FloatPoint[4];
		testMan[0] = new FloatPoint(0.0f, 0.0f);
		testMan[1] = new FloatPoint(800.0f - manSize.getX(), 0.0f);
		testMan[2] = new FloatPoint(0.0f, 480.0f);
		testMan[3] = new FloatPoint(800.0f - manSize.getX(), 480.0f);

		camZeroRect = new FloatRect(0.0f, 0.0f, 10.0f, 25.0f);

		testMouse = new FloatRect(0.0f, 0.0f, 10.0f, 25.0f);
		perspectiveMouse = new FloatPoint();
		worldMouse = new FloatPoint();

		testRect = new FloatRect(640.0f, 80.0f, 120.0f, 80.0f);
		testRectZ1 = new FloatRect(0.0f, 0.0f, 120.0f, 80.0f);
		testRectZ2 = new FloatRect(0.0f, 0.0f, 1.0f, 1.0f);
		testRectZ3 = new FloatRect(380.0f, 290.0f, 120.0f, 80.0f);

		walkingMan = new FloatPoint(400.0f, 300.0f);
		walkingX = 4.0f;
		walkingY = 1.0f;

		movable = new Movable();
		movable.setAngle(290);
		movable.setSpeed(40.0f);

		temp = new FloatPoint();
		temp2 = new FloatPoint();
		cam = new CameraOld(0.0f, 0.0f, 800.0f, 480.0f);

		mush = new ArrayList<Mushroom>();

		tempRect = new FloatRect(30.0f, 30.0f, 100.0f, 50.0f);

		tempMark = new WeatherMark(new FloatRect(90.0f, 40.0f, 180.0f, 180.0f), 20000);

		//		tempMushroom = new FloatRect(10.0f, 90.0f, 35.0f, 42.0f);
		//		tempMushroom2 = new FloatRect();
		//		mushroomColor = new PrismColor();
		mushroomText = new Text();
		mushroomTextPos = new FloatPoint(30.0f, 300.0f);

		//		ta.test();
	}

	public void testConnect()
	{
		try
		{
			//			Socket socket = new Socket("127.0.0.1", NetConstants.port);
			Socket socket = new Socket("192.168.178.24", NetConstants.port);

			Thread.sleep(200);

			ClientLoginMessage_out m = new ClientLoginMessage_out();
			m.setPassword(new Text("JOHN'S PASSWORD!"));
			m.getHeap().ints.add(17);
			m.getHeap().texts.add(new Text("JOHN"));
			m.pack(socket.getOutputStream());

			//			socket.getOutputStream()

			System.out.println("connection to server established");

		}
		catch (Exception e)
		{
			System.err.println("Cannot connect to server");
		}
	}

	//	private void testModelMeta()
	//	{
	//		try
	//		{
	//			MetaFile mf = new MetaFile(new Path("E:\\Prism\\data\\model\\creep"), "fennec.md");
	//			mf.load();
	//			MetaList metaList = mf.getMetaList();
	//
	//			gm = new GridModelMeta();
	//			gm.load(metaList);
	//		}
	//		catch (PrismException e)
	//		{
	//			e.code.print();
	//			e.user.showMessage();
	//		}
	//	}

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

	public void createWindow() throws PrismException
	{
		frameSize = scene.createWindow(900, 500, false);
	}

	public void start() throws PrismException
	{
		try
		// TODO: remove (only temporary)
		{
			testTile = new DrawableGround2[20][20];
			testSubTile = new DrawableGround2[testTile.length][testTile[0].length];
			for (int y = 0; y < testTile.length; ++y)
			{
				for (int x = 0; x < testTile[y].length; ++x)
				{

					int i = (x + y) % (AllGrounds.list.size() - 1);
					int j = i + 1;

					int k = (x - y);
					if (k < 0)
						k = -k;
					k = k % (AllGrounds.list.size() - 1);

					if (x >= 9 && x <= 10)
					{
						i = 3;
						j = 3;
					}
					GroundType2 g1 = AllGrounds.getGroundType(i);
					GroundType2 g2 = AllGrounds.getGroundType(j);
					testTile[y][x] = new DrawableGround2(g1, g2, new Point(x, y), 0.0f);

					testSubTile[y][x] = new DrawableGround2(AllGrounds.getGroundType(k), AllGrounds.getGroundType(k), new Point(x, y),
							-0.1f);

					//					if (y == 3 && x == 7)
					testTile[y][x].setHoleInGround(true);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//		testModelMeta();

		//		IFloatPoint_r screenSize = scene.createWindow(800, 480, false);
		ui.setScreenSize(frameSize);

		connectionHint.setScreenSize(frameSize);

		//		for (int i = 0; i < 10; ++i)
		//		{
		int i = 0;
		UIWindow uiWindow = new UIWindow(null);
		uiWindow.setXRuler(20.0f + i * 20, 0.0f);
		uiWindow.setYRuler(40.0f + i * 5, 0.0f);
		uiWindow.setWidthRuler(150.0f, 0.0f);
		uiWindow.setHeightRuler(90.0f, 0.0f);
		uiWindow.setText(new Text("WINDOW" + i));
		uiWindow.makeClosable(true);
		uiWindow.makeResizable(true);
		uiWindow.setMoveable(true);

		try
		{
			int n = AllArt.mush0.getMeta().getObjectCount();
			for (int j = 0; j < n * 2; ++j)
			{
				int k = j % n;

				//				System.out.println();
				//				System.out.println("mushroom instance " + j);

				Mushroom m = new Mushroom(k, j >= n);
				//				m.setDebug(j == 5);
				m.setPosition(10.0f + 36.0f * j, 170.0f + 10.0f * j);
				m.setTolerableDamage(50);
				m.setLifespan(60);
				m.update(60);
				mush.add(m);
			}
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}

		UIEdit e = new UIEdit();
		e.setWidthRuler(-60.0f, 1.0f);
		e.setXRuler(30.0f, 0.0f);
		e.setYRuler(50.0f, 0.0f);
		uiWindow.addComponent(e);

		ui.addWindow(uiWindow);
		//		}

		for (int j = 0; j < 2; ++j)
		{
			MetaFile mf = Prism.global.loadMetaFile(FileTableDirectoryIndex.WINDOW, j);
			UIWindow w = new UIWindow(scene.getScreenSize());
			w.load(mf.getMetaList());
			ui.addWindow(w);
		}

		scene.initialize();
		slope = scene.getSlope();
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
	public boolean update(int passedMs)
	{
		BlinkColor.selectionColor.update(passedMs); // TODO temp

		tempMark.update(passedMs);

		connectionHint.update(passedMs);

		boolean b = network.update(passedMs);

		if (!isSlopeFixed)
		{
			if (isSlopeGrowing)
			{
				slope += 0.001f;
				if (slope > 1.0f)
				{
					slope = 1.0f;
					isSlopeGrowing = false;
				}
			}
			else
			{
				slope -= 0.001f;
				if (slope < 0.0f)
				{
					slope = 0.0f;
					isSlopeGrowing = true;
				}
			}
			scene.setSlope(slope);
		}

		if (cam.getX() < target.x - 1.0f)
			cam.addX(1.0f);
		else if (cam.getX() > target.x + 1.0f)
			cam.addX(-1.0f);

		if (cam.getY() < target.y - 1.0f)
			cam.addY(1.0f);
		else if (cam.getY() > target.y + 1.0f)
			cam.addY(-1.0f);

		//		if (isMushroomGrowing)
		//		{
		//			mushroomState += 0.001f;
		//			if (mushroomState > 1.0f)
		//			{
		//				mushroomState = 1.0f;
		//				isMushroomGrowing = false;
		//			}
		//		}
		//		else
		//		{
		//			mushroomState -= 0.001f;
		//			if (mushroomState < 0.0f)
		//			{
		//				mushroomState = 0.0f;
		//				isMushroomGrowing = true;
		//			}
		//		}

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

		float seconds = 0.001f * passedMs;
		movable.timeUpdate(passedMs, seconds);

		nis += passedMs;
		if (nis > 1000)
		{
			nis = 0;
			if (++ni > 2)
				ni = 0;
		}

		camZeroRect.setX(cam.getX());
		camZeroRect.setY(cam.getY());

		return clientException == null && !exitGameRequested;
	}

	@Override
	public void drawWorld(IWorldRenderer2 ren) throws PrismException
	{
		AllShaders.color.activate();
		AllShaders.color.setTexture(AllArt.env0.getTexture(0));

		ren.reset();
		ren.setTexInfo(0, AllArt.env0.getMeta().getRect(1));
		testPlane2.copyFrom(testPlane);
		for (int y = 0; y < 10; ++y)
		{
			for (int x = 0; x < 10; ++x)
			{
				ren.setSprite(testPlane2);
				testPlane2.addX(120.0f);
				ren.drawPlane(1);
			}
			testPlane2.setX(testPlane.getX());
			testPlane2.addY(80.0f);
		}

		AllShaders.color.activate();
		AllShaders.color.setTexture(AllArt.mush0.getTexture(0));

		ren.reset();
		//		testSpriteTemp.setTopLeft(testSprite.getTopLeft());
		//		for (int i = 0; i < AllArt.mush0.getMeta().getObjectCount(); ++i)
		//		{
		//			ren.setupTexture(0, AllArt.mush0.getTextureInfo(0, i, 0, 0));
		//			ren.setupSprite(testSpriteTemp);
		//			ren.drawObject(1);
		//			testSpriteTemp.addX(39.0f);
		//			testSpriteTemp.addY(10.0f);
		//		}

		ren.setTexInfo(0, AllArt.mush0.getTextureInfo(0, 1, 0, 0));
		testSpriteTemp.setTopLeft(0.0f, 0.0f);
		ren.setSprite(testSpriteTemp);
		ren.drawObject(1);
		//
		ren.setTexInfo(0, AllArt.mush0.getTextureInfo(0, 2, 0, 0));
		testSpriteTemp.setTopLeft(120.0f, 0.0f);
		ren.setSprite(testSpriteTemp);
		ren.drawObject(1);
		//
		ren.setTexInfo(0, AllArt.mush0.getTextureInfo(0, 3, 0, 0));
		testSpriteTemp.setTopLeft(0.0f, 80.0f);
		ren.setSprite(testSpriteTemp);
		ren.drawObject(1);
		//
		ren.setTexInfo(0, AllArt.mush0.getTextureInfo(0, 0, 0, 0));
		testSpriteTemp.setTopLeft(120.0f, 80.0f);
		ren.setSprite(testSpriteTemp);
		ren.drawObject(1);

		ren.setSprite(camZeroRect);
		ren.drawObject(1);

		ren.setSprite(testMouse);
		ren.drawObject(1);

		/* ******************** */

		/* ********************
		 * 
		 * 
		
		// draw edges
		//		renderer.setColor(0.3f, 0.5f, 0.2f);
		if (isDebugDecoActive)
		{
			cam.transformPosition(manSize, temp2);
			for (int i = 0; i < 4; ++i)
			{
				cam.transformPosition(testMan[i], temp);
				texInfo = AllArt.frames.getTextureInfo(0, 0, 1, ni);

				renderer.drawObject(texInfo, temp, temp2, 0.0f);
			}
		}

		// draw walking man
		//		renderer.setColor(0.3f, 0.2f, 0.8f);
		if (isDebugDecoActive)
		{
			cam.transformPosition(walkingMan, temp);
			texInfo = AllArt.frames.getTextureInfo(0, 0, 2, ni);
			renderer.drawObject(texInfo, temp, temp2, 0.0f);
		}

		// draw movable
		//		renderer.setColor(0.3f, 0.2f, 0.5f);
		if (isDebugDecoActive)
		{
			cam.transformPosition(movable.getPosition(), temp);
			texInfo = AllArt.frames.getTextureInfo(0, 0, 3, ni);
			renderer.drawObject(texInfo, temp, temp2, 0.0f);
		}

		//		ITexture tex = Materials.env0.getTexture(0);
		//		IFloatRect_r texRect = Materials.env0.getMeta().getRect(0);
		//		AllShaders.defaultShader.activate();
		//		AllShaders.defaultShader.setTexture(tex);
		//		cam.transformWithCameraBounds(testGround, tempRect);
		//		renderer.drawPlane(texRect, tempRect);

		for (int y = 0; y < testTile.length; ++y)
			for (int x = 0; x < testTile[y].length; ++x)
				testSubTile[y][x].draw(renderer, cam);

		for (int y = 0; y < testTile.length; ++y)
			for (int x = 0; x < testTile[y].length; ++x)
				testTile[y][x].draw(renderer, cam);

		if (isDebugDecoActive)
			tempMark.draw(renderer, cam);

		// draw mushrooms
		//		tempMushroom2.copyFrom(tempMushroom);
		//		tempMushroom2.multiplySize(0.7f * (float) Math.sqrt(mushroomState));
		//		tempMushroom2.multiplyHeight(mushroomState);
		//		mushroomColor.set(0.3f + mushroomState * 0.7f, 0.3f + mushroomState * 0.7f, 0.3f + mushroomState * 0.7f, 0.7f * (float) Math
		//				.sqrt(mushroomState));
		//		AllShaders.defaultShader.activate();
		//		AllShaders.defaultShader.setColor(mushroomColor);
		//
		//		for (int i = 0; i < 3; ++i)
		//		{
		//			for (int j = 0; j < 9; ++j)
		//			{
		//				texInfo = Materials.mush0.getTextureInfo(0, 0, i, j == 0 ? 1 : 0);
		//				AllShaders.defaultShader.setTexture(texInfo.getTexture());
		//
		//				cam.transformWithCameraBounds(tempMushroom2, tempRect);
		//				renderer.drawObject(texInfo, tempRect.getTopLeft(), tempRect.getSize(), 0.0f);
		//
		//				texInfo.flipH();
		//
		//				tempMushroom2.addX(tempMushroom.getWidth());
		//				tempMushroom2.addY(5.0f);
		//			}
		//			tempMushroom2.setX(tempMushroom.getX());
		//			tempMushroom2.addY(tempMushroom.getHeight());
		//		}

		//		System.out.println();
		//		System.out.println("----");

		for (int i = 0; i < mush.size(); ++i)
		{
			Mushroom m = mush.get(i);
			//			System.out.println();
			//			System.out.println(m.toString());

			//			System.out.println("mushroom " + i);

			m.draw(renderer, cam);
		}

		//		System.out.println(".");

		if (isDebugDecoActive)
		{
			texInfo = AllArt.frames.getTextureInfo(0, 3, 0, 0);
			AllShaders.color.activate();
			AllShaders.color.setTexture(texInfo.getTexture());
			AllShaders.color.setColor(PrismColor.opaqueWhite);
			cam.transformPosition(testMouse.getTopLeft(), temp);
			cam.transformPosition(testMouse.getSize(), temp2);
			renderer.drawObject(texInfo, temp, temp2, 0.0f);
		}

		// simulate a mask ground
		IFloatRect_r texRectA = AllArt.env0.getMeta().getRect(1);
		int n = AllArt.groundMasks.getMeta().getSpriteCount();
		int i = Calendar.getInstance().get(Calendar.SECOND) % n;
		IFloatRect_r texRectM = AllArt.groundMasks.getMeta().getRect(i);
		cam.transformRect(testRect, tempRect);
		AllShaders.mask.activate();
		AllShaders.mask.setTexture(AllArt.env0.getTexture(0));
		AllShaders.mask.setMask(AllArt.groundMasks.getTexture(0));
		renderer.drawMaskPlane(texRectA, texRectM, tempRect);
		
		
		** ********************  */
	}

	@Override
	public void drawUI(IUIRenderer2 ren) throws PrismException
	{
		ui.draw(ren);

		/* **************************
		// test art

		//		testRect.addY(-0.1f);
		//		renderer.drawSprite(texInfo, testRect);

		//		Materials.printer.setText(testText);
		//		Materials.printer.print(testPosition, fontScale);

		//		uiWindow.setWidth(uiWindow.getWidth() + 0.06f);
		//		uiWindow.setHeight(uiWindow.getHeight() + 0.02f);
		//		uiWindow.draw(renderer);

		//		ta.draw(renderer);

		//		AllShaders.color.activate();
		//				ui.draw(renderer);

		testText.set("SLOPE=" + slope);
		AllTools.printer.setText(testText);
		AllTools.printer.print(tempRect.getTopLeft());
		//		tempRect

		if (network.isConnecting())
		{
			connectionHint.draw(renderer);
		}

		mushroomText.set("GROWTH: " + mush.get(0).getCurrentGrowth() + "/" + mush.get(0).getLifespan() + ", DAMAGE: "
				+ mush.get(0).getCurrentDamage() + (mush.get(0).isDead() ? ", DEAD" : ""));
		AllTools.printer.setText(mushroomText);
		AllTools.printer.setColor(PrismColor.opaqueWhite);
		AllTools.printer.print(mushroomTextPos);

		// ----------------

		//		AllBuffers.groundBuffer.activate();
		//		AllShaders.defaultShader.activate();
		//		AllShaders.defaultShader.setTexture(AllArt.env0.getTexture(0));
		//		int n = AllArt.env0.getMeta().getSpriteCount();
		//		int i = Calendar.getInstance().get(Calendar.SECOND) % n;
		//		renderer.drawSprite(AllArt.env0.getMeta().getRect(i), testRectZ1);
		//		AllBuffers.groundBuffer.resetToUI();
		//
		//		AllShaders.defaultShader.activate();
		//		AllShaders.defaultShader.setTexture(AllBuffers.groundBuffer.getTextureID());
		//		renderer.drawSprite(AllBuffers.groundBuffer.getRect(), testRectZ3);

		//		AllShaders.defaultShader.activate();
		//		AllShaders.defaultShader.setTexture(AllArt.env0.getTexture(0));
		//		int n = AllArt.env0.getMeta().getSpriteCount();
		//		int i = Calendar.getInstance().get(Calendar.SECOND) % n;
		//		AllBuffers.groundBuffer.activate();
		//		AllBuffers.groundBuffer.drawSprite(AllArt.env0.getMeta().getRect(i), testRectZ1);
		//		//		renderer.drawSprite(AllArt.env0.getMeta().getRect(i), testRectZ1);
		//		AllBuffers.groundBuffer.resetToUI();
		

		//		AllBuffers.groundBuffer.activate();
		//		AllBuffers.groundBuffer.drawBlue();
		//		AllBuffers.groundBuffer.resetToUI();
		//		AllShaders.defaultShader.activate();
		//		AllShaders.defaultShader.setTexture(AllBuffers.groundBuffer.getTextureID());
		//		renderer.drawSprite(AllBuffers.groundBuffer.getRect(), testRectZ3);
		
		****************************************** */

		AllShaders.color.activate();
		AllTools.printer.setText(mouseText1);
		AllTools.printer.print(mouseTextPos1);
		AllTools.printer.setText(mouseText2);
		AllTools.printer.print(mouseTextPos2);
	}

	@Override
	public void onMouseMove()
	{
		ui.onMouseMove();

		mouseText1.set("SCREEN: X=" + (int) mouseOnScreen.getX() + ", Y=" + (int) mouseOnScreen.getY());
		mouseText2.set("WORLD: X=" + (int) mouseInWorld.getX() + ", Y=" + (int) mouseInWorld.getY());
	}

	@Override
	public boolean onMouseDown(int button)
	{
		boolean b = false;

		if (button == MouseButton.left)
			b = ui.onMouseDown(button);

		return true;
	}

	@Override
	public void onMouseUp(int button)
	{
		if (button == MouseButton.left)
			ui.onMouseUp(button);
	}

	@Override
	public boolean onControlKey(int keyCode, boolean isDown)
	{
		boolean b = ui.onControlKey(keyCode, isDown);

		if (!b && isDown)
		{
			if (keyCode == KeyEvent.VK_DOWN)
				target.y += 160.0f;
			else if (keyCode == KeyEvent.VK_UP)
				target.y -= 160.0f;
			else if (keyCode == KeyEvent.VK_LEFT)
				target.x -= 120.0f;
			else if (keyCode == KeyEvent.VK_RIGHT)
				target.x += 120.0f;
		}

		return true;
	}

	@Override
	public boolean onCharacterKey(char c, boolean isDown)
	{
		ui.onCharacterKey(c, isDown);

		if (c == 'P')
			for (int y = 0; y < testTile.length; ++y)
				for (int x = 0; x < testTile[y].length; ++x)
					testTile[y][x].getWeatherFader().setWeather(0);

		if (c == 'L')
			isSlopeFixed = !isSlopeFixed;

		if (c == 'W')
		{
			slope += 0.05f;
			scene.setSlope(slope);
		}

		if (c == 'Y')
			isDebugDecoActive = !isDebugDecoActive;

		for (Mushroom m : mush)
		{
			if (c == 'M')
				m.update(2);
			else if (c == 'N')
				m.update(-2);
			else if (c == 'O')
				m.reset();
			else if (c == 'D')
				m.damage(5);
		}

		if (c == 'Q')
		{
			mush.clear();
			try
			{
				float y = 40.0f;

				for (int i = 0; i < 180; ++i)
				{
					int o = (int) (Math.random() * AllArt.mush0.getMeta().getObjectCount());
					boolean flipH = Math.random() < 0.5;

					float x = (float) (Math.random() * 400.0f);

					Mushroom m = new Mushroom(o, flipH);
					m.setPosition(x, y);
					m.setTolerableDamage(50);
					m.setLifespan(60);
					m.update(25 + (int) (Math.random() * 35));
					mush.add(m);

					y += 2.0f + Math.random() * 3.0f;
				}
			}
			catch (PrismException e)
			{
				e.code.print();
				e.user.showMessage();
			}
		}

		for (int i = 0; i < testTile.length; ++i)
		{
			for (int j = 0; j < testTile[i].length; ++j)
			{
				DrawableGround2 t = testTile[i][j];

				if (c == 'S')
					t.getWeatherFader().applySnow(20);
				else if (c == 'R')
					t.getWeatherFader().applyRain(20);
				else if (c == 'F' || c == 'H' || c == 'B')
					t.getWeatherFader().applyHeat(20);
			}
		}
		return true;
	}

	@Override
	public void executeExitGame()
	{
		exitGameRequested = true;
	}

	@Override
	public void executeCloseWindow(UIWindow w)
	{
		ui.closeWindow(w);
	}

	@Override
	public void requestLoginScreen()
	{
		try
		{
			MetaFile mf = Prism.global.loadMetaFile(FileTableDirectoryIndex.WINDOW, 0);
			UIWindow w = new UIWindow(scene.getScreenSize());
			w.load(mf.getMetaList());
			ui.addWindow(w);
		}
		catch (Exception e)
		{
			if (clientException == null)
				clientException = e;
		}
	}

	// IExceptionSink:
	@Override
	public void receiveException(Exception e)
	{
		if (clientException == null)
			clientException = e;
	}
}
