package xoric.prism.scene.camera;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.ICameraTransform;

public class Camera extends FloatRect implements ICameraTransform
{
	public Camera(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		super(topLeft, size);
	}

	public Camera(float x, float y, float width, float height)
	{
		super(x, y, width, height);
	}

	@Override
	public void transformWithCameraBounds(IFloatPoint_r in, FloatPoint out)
	{
		out.x = (in.getX() - topLeft.x) / size.getX();
		out.y = (in.getY() - topLeft.y) / size.getY();
	}

	@Override
	public void transformWithCameraBounds(IFloatRect_r in, FloatRect out)
	{
		out.copyFrom(in);
		out.subtractPosition(topLeft);
		out.divideAll(size);
	}

	@Override
	public void transformFrameFractionToWorld(IFloatPoint_r in, FloatPoint out)
	{
		out.copyFrom(in);
		out.multiply(size);
		out.add(topLeft);
	}
}
