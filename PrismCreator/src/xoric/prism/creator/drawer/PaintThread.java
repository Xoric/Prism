package xoric.prism.creator.drawer;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.scene.IRenderer;
import xoric.prism.scene.IScene;
import xoric.prism.scene.ISceneListener;
import xoric.prism.scene.SceneStage;

public class PaintThread implements Runnable, ISceneListener
{
	private final IScene scene;

	FloatPoint pos = new FloatPoint(0.1f, 0.2f);
	FloatPoint size = new FloatPoint(0.4f, 0.6f);

	private float x, y, w, h;

	public PaintThread(IScene scene)
	{
		this.scene = scene;

		x = 0.2f;
		y = 0.2f;
		w = 0.5f;
		h = 0.6f;
	}

	@Override
	public void run()
	{
		scene.createWindow(640, 480, false);
		scene.startLoop(this);
		scene.setStage(SceneStage.INTERFACE);
	}

	@Override
	public boolean requestUpdateScene(int passedMs, IRenderer renderer)
	{
		scene.setStage(SceneStage.GROUND);
		renderer.setColor(0.8f, 0.2f, 0.2f);
		renderer.drawPlane(pos, size);

		scene.setStage(SceneStage.INTERFACE);
		renderer.setColor(0.8f, 0.2f, 0.2f);

		x += (Math.random() < 0.5 ? -1.0f : 1.0f) * 0.001f;
		y += (Math.random() < 0.5 ? -1.0f : 1.0f) * 0.001f;
		w += (Math.random() < 0.5 ? -1.0f : 1.0f) * 0.001f;
		h += (Math.random() < 0.5 ? -1.0f : 1.0f) * 0.001f;

		renderer.drawSprite(x, y, w, h);

		return true;
	}

	@Override
	public void onClosingScene()
	{
		System.out.println("closing scene");
	}
}
