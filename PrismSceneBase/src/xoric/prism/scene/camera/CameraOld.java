package xoric.prism.scene.camera;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public class CameraOld extends Camera
{
	public CameraOld(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		super(topLeft, size);
	}

	public CameraOld(float x, float y, float width, float height)
	{
		super(x, y, width, height);
	}

	@Override
	public void transformPosition(IFloatPoint_r in, FloatPoint out)
	{
		out.x = (in.getX() - topLeft.x) / size.getX();
		out.y = (in.getY() - topLeft.y) / size.getY();
	}

	@Override
	public void transformSize(IFloatPoint_r in, FloatPoint out)
	{
		out.copyFrom(in);
		out.divide(size);
	}

	@Override
	public void transformRect(IFloatRect_r in, FloatRect out)
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
