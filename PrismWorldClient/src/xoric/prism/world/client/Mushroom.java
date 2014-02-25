package xoric.prism.world.client;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.scene.materials.art.AllArt;

/**
 * @author XoricLee
 * @since 19.02.2014, 16:35:25
 */
public class Mushroom extends DrawableGrowth
{
	public Mushroom(int objectIndex, boolean flipH) throws PrismException
	{
		this.setTextures(AllArt.mush0, objectIndex, flipH);
		this.setFullSize(AllArt.mush0.getMeta().getObject(objectIndex).getSize());
	}

	@Override
	protected float calcWidthFactor(float f)
	{
		//		return 0.8f * f;
		return f;
	}

	@Override
	protected float calcHeightFactor(float f)
	{
		//		return 0.7f * f;

		return 0.4f * f + 0.5f * (f * f);
		//		return f;
	}

	@Override
	protected void updateColors(float f, float d)
	{
		float c = 0.3f + 0.7f * f;
		c *= 0.8f + 0.2f * (1.0f - d);
		float a = (float) Math.sqrt(f);

		currentColor.set(c, c, c, a);

		//		c = 1.0f - 0.8f * (float) Math.sqrt(Math.sqrt(d));
		currentDamageColor.set(currentColor);
		currentDamageColor.setAlpha(d);
	}

	/**
	 * @return
	 */
	@Deprecated
	public int getCurrentGrowth()
	{
		return currentGrowth;
	}

	@Deprecated
	public int getLifespan()
	{
		return lifespanSeconds;
	}

	@Deprecated
	public int getCurrentDamage()
	{
		return currentDamage;
	}
}
