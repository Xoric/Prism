package xoric.prism.world.entities;

import xoric.prism.data.FloatPoint;
import xoric.prism.data.IFloatPoint_r;
import xoric.prism.data.IText_r;
import xoric.prism.data.Text;

public class Entity
{
	private FloatPoint position;
	private Text name;

	public Entity()
	{
		position = new FloatPoint();
		name = new Text();
	}

	public void move(float dX, float dY)
	{
		position.x += dX;
		position.y += dY;
	}

	public IFloatPoint_r getPosition()
	{
		return position;
	}

	public IText_r getName()
	{
		return name;
	}
}
