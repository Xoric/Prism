package xoric.prism.creator.drawer;

import xoric.prism.scene.IScene;

class ModelPaint
{
	private final PaintThread paintThread;

	public ModelPaint(IScene scene)
	{
		paintThread = new PaintThread(scene);
	}

	public void start()
	{
		Thread t = new Thread(paintThread);
		t.start();
	}
}
