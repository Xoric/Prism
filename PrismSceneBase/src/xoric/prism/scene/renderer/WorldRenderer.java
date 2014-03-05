package xoric.prism.scene.renderer;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.art.hotspots.Marker;
import xoric.prism.scene.camera.ICameraTransform;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:01:39
 */
public abstract class WorldRenderer extends BaseRenderer implements IWorldRenderer2
{
	private ICameraTransform cam;
	private boolean useTransform;

	private final FloatPoint transformedPos;
	private final FloatPoint transformedSize;

	private IFloatPoint_r screenPos;
	private IFloatPoint_r spriteSize;
	private Marker marker;

	public WorldRenderer(boolean transformGL)
	{
		super(transformGL);
		useTransform = transformGL;

		transformedPos = new FloatPoint();
		transformedSize = new FloatPoint();
	}

	protected IFloatPoint_r getScreenPos()
	{
		return useTransform ? transformedPos : screenPos;
	}

	protected IFloatPoint_r getSpriteSize()
	{
		return useTransform ? transformedSize : spriteSize;
	}

	protected Marker getMarker()
	{
		return marker;
	}

	@Override
	public void setCamera(ICameraTransform cam)
	{
		this.cam = cam;
		this.useTransform = cam != null;
		//		this.useTransform = transformGL || cam != null;
	}

	@Override
	public void reset()
	{
		super.reset();

		screenPos = null;
		spriteSize = null;
		marker = null;
	}

	@Override
	public void setSprite(IFloatRect_r spriteRect)
	{
		setSprite(spriteRect.getTopLeft(), spriteRect.getSize());
	}

	@Override
	public void setSprite(IFloatPoint_r screenPos, IFloatPoint_r spriteSize)
	{
		if (useTransform)
		{
			if (cam != null)
			{
				cam.transformPosition(screenPos, transformedPos);
				cam.transformSize(spriteSize, transformedSize);
			}
			else
			{
				transformedPos.copyFrom(screenPos);
				transformedSize.copyFrom(spriteSize);
			}

			//			if (transformGL)
			//			{
			//				transformedPos.y -= transformedSize.y;
			//				transformedSize.y = -transformedSize.y;
			//			}
		}
		else
		{
			this.screenPos = screenPos;
			this.spriteSize = spriteSize;
		}
	}

	public void setMarker(Marker m)
	{
		this.marker = m;
	}
}
