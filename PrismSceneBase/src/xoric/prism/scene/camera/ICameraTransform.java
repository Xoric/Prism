package xoric.prism.scene.camera;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

public interface ICameraTransform
{
	public void transformPosition(IFloatPoint_r in, FloatPoint out);

	public void transformSize(IFloatPoint_r in, FloatPoint out);

	public void transformRect(IFloatRect_r in, FloatRect out);

	public void transformNormalizedScreenToWorld(IFloatPoint_r in, FloatPoint out);
}
