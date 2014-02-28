package xoric.prism.scene.camera;

import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;

/**
 * @author XoricLee
 * @since 28.02.2014, 03:09:05
 */
public abstract class Camera extends FloatRect implements ICameraTransform
{
	public Camera(IFloatPoint_r topLeft, IFloatPoint_r size)
	{
		super(topLeft, size);
	}

	public Camera(float x, float y, float width, float height)
	{
		super(x, y, width, height);
	}
}
