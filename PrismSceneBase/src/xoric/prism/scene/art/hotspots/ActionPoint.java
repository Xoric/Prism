package xoric.prism.scene.art.hotspots;

import xoric.prism.data.physics.IAngle_r;
import xoric.prism.data.types.IFloatPoint_r;

/**
 * @author XoricLee
 * @since 28.02.2014, 17:03:53
 */
public class ActionPoint //implements IActionPoint_rIStackable, IActionPoint_r
{
	public final IFloatPoint_r position;
	public final IAngle_r angle;

	public ActionPoint(IFloatPoint_r position, IAngle_r angle)
	{
		this.position = position;
		this.angle = angle;
	}

	public IFloatPoint_r getPosition()
	{
		return position;
	}

	public IAngle_r getAngle()
	{
		return angle;
	}

	//	@Override
	//	public void appendTo(Heap_out h)
	//	{
	//		h.ints.add((int) position.x);
	//		h.ints.add((int) position.y);
	//		angle.appendTo(h);
	//	}
	//
	//	@Override
	//	public void extractFrom(Heap_in h) throws PrismException
	//	{
	//		position.x = h.nextInt();
	//		position.y = h.nextInt();
	//		angle.extractFrom(h);
	//	}
}
