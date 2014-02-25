package xoric.prism.world.entities;

import xoric.prism.data.physics.Angle;
import xoric.prism.data.types.IFloatPoint_r;

public class Movable extends Entity implements IContinuous
{
	private Angle angle;
	private float speed;

	public Movable()
	{
		angle = new Angle();
	}

	@Override
	public void timeUpdate(int passedMs, float seconds)
	{
		IFloatPoint_r components = angle.getComponents();

		float s = seconds * speed;
		float moveX = s * components.getX();
		float moveY = s * components.getY();

		this.move(moveX, moveY);
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public void setAngle(int degree)
	{
		angle.set(degree);
	}
}
