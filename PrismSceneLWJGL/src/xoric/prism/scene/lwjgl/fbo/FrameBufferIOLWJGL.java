package xoric.prism.scene.lwjgl.fbo;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.scene.fbo.IFrameBuffer;
import xoric.prism.scene.fbo.IFrameBufferIO;
import xoric.prism.scene.lwjgl.cleanup.TrashCan;

public class FrameBufferIOLWJGL implements IFrameBufferIO
{
	private final IFrameBufferParent scene;

	public FrameBufferIOLWJGL(IFrameBufferParent scene)
	{
		this.scene = scene;
	}

	// IFrameBufferIO:
	@Override
	public IFrameBuffer createFrameBuffer(FloatPoint size) throws PrismException
	{
		FrameBuffer2 f = new FrameBuffer2(scene, size);
		f.initialize();
		TrashCan.addResource(f);
		return f;
	}
}
