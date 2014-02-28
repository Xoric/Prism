package xoric.prism.scene.camera;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public class CameraGL extends Camera
{
	public CameraGL(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		super(topLeft, size);
	}

	public CameraGL(float x, float y, float width, float height)
	{
		super(x, y, width, height);
	}

	@Override
	public void transformPosition(IFloatPoint_r in, FloatPoint out)
	{
		out.x = -0.5f + (in.getX() - topLeft.x) / size.getX();
		out.y = 0.5f - (in.getY() - topLeft.y) / size.getY();
	}

	@Override
	public void transformSize(IFloatPoint_r in, FloatPoint out)
	{
		out.copyFrom(in);
		out.divide(size);
		out.y = -out.y;
	}

	@Override
	public void transformRect(IFloatRect_r in, FloatRect out)
	{
		float x = -0.5f + (in.getX() - topLeft.x) / size.getX();
		float y = 0.5f - (in.getY() - topLeft.y) / size.getY();
		float w = in.getWidth() / size.x;
		float h = -in.getHeight() / size.y;
		out.set(x, y, w, h);
	}

	@Override
	public void transformFrameFractionToWorld(IFloatPoint_r in, FloatPoint out)
	{
		out.copyFrom(in);
		out.multiply(size);
		out.add(topLeft);
	}
}
