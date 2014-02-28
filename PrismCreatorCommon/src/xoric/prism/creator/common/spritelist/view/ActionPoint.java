package xoric.prism.creator.common.spritelist.view;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.physics.Angle;
import xoric.prism.data.types.FloatPoint;

/**
 * @author XoricLee
 * @since 28.02.2014, 17:03:53
 */
public class ActionPoint implements IStackable
{
	public final FloatPoint down;
	public Angle angle;

	public ActionPoint()
	{
		this.down = new FloatPoint();
		this.angle = new Angle();
	}

	public ActionPoint(FloatPoint down, FloatPoint up)
	{
		this.down = down;
		this.angle = new Angle();
		this.angle.set(down, up);
	}

	public ActionPoint(FloatPoint down, Angle angle)
	{
		this.down = down;
		this.angle = angle;
	}

	// IStackable_out:
	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add((int) down.x);
		h.ints.add((int) down.y);
		angle.appendTo(h);
	}

	// IStackable_in:
	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		down.x = h.nextInt();
		down.y = h.nextInt();
		angle.extractFrom(h);
	}
}
