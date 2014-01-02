package xoric.prism.world.entities;

import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;

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
