package xoric.prism.scene.renderer;

import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.art.TextureInfo;

/**
 * @author XoricLee
 * @since 26.02.2014, 16:16:51
 */
abstract class BaseRenderer implements IBaseRenderer2
{
	private static final int slots = 3;

	protected final boolean transformGL; // true: texture Y-coordinates will be transformed: Y -> 1.0f - Y

	private final IFloatRect_r[] texRects;
	private final FloatRect[] transformedRects;
	private final boolean[] useTransform;
	private float z;

	public BaseRenderer(boolean transformGL)
	{
		this.texRects = new IFloatRect_r[slots];
		this.transformedRects = new FloatRect[slots];
		this.useTransform = new boolean[slots];
		this.transformGL = transformGL;

		for (int i = 0; i < slots; ++i)
			transformedRects[i] = new FloatRect();
	}

	protected IFloatRect_r getTexRect(int index)
	{
		return useTransform[index] ? transformedRects[index] : texRects[index];
	}

	protected float getZ()
	{
		return z;
	}

	@Override
	public void reset()
	{
		for (int i = 0; i < slots; ++i)
		{
			texRects[i] = null;
			useTransform[i] = false;
		}
		z = 0.0f;
	}

	@Override
	public void setZ(float z)
	{
		this.z = z;
	}

	@Override
	public void flipTextureH(int index)
	{
		transformedRects[index].copyAndFlip(texRects[index], true, false);
		useTransform[index] = true;
	}

	@Override
	public void setTexInfo(int index, TextureInfo texInfo)
	{
		setTexInfo(index, texInfo.getRect(), texInfo.isFlippedH());
	}

	@Override
	public void setTexInfo(int index, IFloatRect_r texRect)
	{
		setTexInfo(index, texRect, false);
	}

	@Override
	public void setTexInfo(int index, IFloatRect_r texRect, boolean flipH)
	{
		boolean useTransform = flipH || transformGL;
		this.useTransform[index] = useTransform;

		if (useTransform)
			transformedRects[index].copyAndTransformGL(texRect, flipH, transformGL);
		//			transformedRects[index].copyAndFlip(texRect, true, false);
		else
			texRects[index] = texRect;
	}
}
