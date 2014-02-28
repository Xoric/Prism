package xoric.prism.scene.renderer;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:01:39
 */
public abstract class UIRenderer extends BaseRenderer implements IUIRenderer2
{
	private IFloatRect_r spriteRect;

	private final FloatPoint screenSize;
	private final FloatRect transformedRect;
	private boolean useTransform;

	public UIRenderer(boolean transformGL)
	{
		super(transformGL);

		screenSize = new FloatPoint(1.0f, 1.0f);
		useTransform = false;
		transformedRect = new FloatRect();
	}

	protected IFloatRect_r getScreenRect()
	{
		return useTransform ? transformedRect : spriteRect;
	}

	@Override
	public void setScreenSize(IFloatPoint_r size)
	{
		screenSize.copyFrom(size);
		useTransform = size.getX() < 0.999f || size.getX() > 1.001f || size.getY() < 0.999f || size.getY() > 1.001f;
	}

	@Override
	public void reset()
	{
		super.reset();

		spriteRect = null;
	}

	@Override
	public void setupSprite(IFloatRect_r spriteRect)
	{
		if (useTransform)
		{
			transformedRect.copyFrom(spriteRect);
			transformedRect.divideAll(screenSize);
		}
		else
		{
			this.spriteRect = spriteRect;
		}
	}
}
