package xoric.prism.world.growth;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;

/**
 * @author XoricLee
 * @since 19.02.2014, 16:14:32
 */
public class Growth
{
	// class specific
	protected int lifespanSeconds;
	protected int tolerableDamage;

	// live consistent
	protected final FloatPoint position;

	// live volatile
	protected int currentDamage;
	protected int currentGrowth;
	protected boolean isGrowing;
	protected boolean isDead;

	public Growth()
	{
		this.position = new FloatPoint();
		this.isGrowing = true;
	}

	public void setLifespan(int lifespanSeconds)
	{
		this.lifespanSeconds = lifespanSeconds;
	}

	public void setTolerableDamage(int tolerableDamage)
	{
		this.tolerableDamage = tolerableDamage;
	}

	public void setPosition(IFloatPoint_r position)
	{
		this.position.x = position.getX();
		this.position.y = position.getY();
	}

	public void setPosition(float x, float y)
	{
		this.position.x = x;
		this.position.y = y;
	}

	/**
	 * 
	 */
	@Deprecated
	public void reset() // TODO remove
	{
		this.currentGrowth = 0;
		this.isGrowing = true;
		this.currentDamage = 0;
		isDead = false;
		update(0);
		damage(0);
	}

	public void damage(int damage)
	{
		this.currentDamage += damage;
		if (this.currentDamage > 100)
			currentDamage = 100;

		this.isGrowing &= this.currentDamage <= tolerableDamage;
	}

	public boolean isDead()
	{
		return isDead;
	}

	public void update(int passedSeconds)
	{
		if (isGrowing)
		{
			currentGrowth += passedSeconds;

			if (currentGrowth >= lifespanSeconds)
			{
				currentGrowth = lifespanSeconds;
				isGrowing = false;
			}
		}
		else
		{
			currentGrowth -= passedSeconds;

			if (currentGrowth < 0)
			{
				currentGrowth = 0;
				isDead = true;
			}
		}
	}
}
