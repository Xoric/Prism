package xoric.prism.world.movement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.types.Booleans;

/**
 * @author XoricLee
 * @since 30.10.2011, 13:36:17
 */
public class MoveCaps implements IPackable, IStackable
{
	private Booleans bools;

	/**
	 * MoveCaps constructor. All capabilities are initialized as false.
	 * @throws BooleansException
	 */
	public MoveCaps()
	{
		bools = new Booleans();
	}

	/**
	 * Copies this MoveCaps object internal data from another.
	 * @param other
	 */
	public MoveCaps(MoveCaps other)
	{
		this.bools = new Booleans(other.toInt());
	}

	public MoveCaps(MoveType... moveTypes)
	{
		bools = new Booleans();
		for (MoveType t : moveTypes)
			bools.setValue(t.ordinal(), true);
	}

	public boolean[] toBools()
	{
		return bools.toBools(MoveType.COUNT);
	}

	/**
	 * Applies the bitwise-and operator for the given MoveCaps object to this one.
	 * @param caps
	 */
	public void bitwiseAnd(MoveCaps caps)
	{
		bools.setValues(this.toInt() & caps.toInt());
	}

	/**
	 * Returns a list of MoveTypes enabled in this MoveCaps object.
	 * @return List<MoveType>
	 */
	public List<MoveType> getCapabilities()
	{
		List<MoveType> result = new ArrayList<MoveType>();

		for (MoveType t : MoveType.VALUES)
			if (isCapableOf(t))
				result.add(t);

		return result;
	}

	/**
	 * Creates a MoveCaps object combining a given set of MoveCaps objects with the bitwise-and operator. Each MoveType in the resulting
	 * MoveCaps object is true if and only if all of the given MoveCaps are true for this MoveType.
	 * @param caps
	 *            Set of MoveCaps
	 */
	public MoveCaps(MoveCaps... caps)
	{
		int values = Booleans.ALL_TRUE;
		for (MoveCaps c : caps)
			values = values & c.toInt();
		this.bools = new Booleans(values);
	}

	public MoveCaps(int caps)
	{
		bools = new Booleans(caps);
	}

	public boolean isCapableOf(MoveType moveType)
	{
		return bools.getValue(moveType.ordinal());
	}

	public boolean isAccessibleFor(MoveCaps caps)
	{
		for (MoveType m : MoveType.VALUES)
			if (this.isCapableOf(m) && caps.isCapableOf(m))
				return true;

		return false;
	}

	public void setCapabilities(int caps)
	{
		bools.setValues(caps);
	}

	public void setAllCapabilities(boolean value)
	{
		bools.setAllValues(value);
	}

	public void setCapabilities(MoveType... values)
	{
		bools.setValues(0);
		for (MoveType moveType : values)
			bools.setValue(moveType.ordinal(), true);
	}

	public void setCapabilities(boolean... values)
	{
		bools.setValues(0);
		int i = 0;
		for (boolean b : values)
			bools.setValue(i++, b);
	}

	public void setCapability(MoveType moveType, boolean value)
	{
		bools.setValue(moveType.ordinal(), value);
	}

	@Override
	public String toString()
	{
		boolean isFirst = true;
		StringBuffer sb = new StringBuffer();
		for (MoveType t : MoveType.VALUES)
		{
			if (this.isCapableOf(t))
			{
				sb.append((isFirst ? "" : ", ") + t.toString());
				isFirst = false;
			}
		}
		if (isFirst)
			sb.append("none");

		return sb.toString();
	}

	/**
	 * Returns an int representing this MoveCaps object.
	 * @return int
	 */
	public int toInt()
	{
		return bools.toInt();
	}

	/**
	 * @return Returns if any MoveType is true.
	 */
	public boolean anyIsTrue()
	{
		return bools.anyIsTrue();
	}

	@Override
	public void pack(OutputStream stream) throws IOException, PrismException
	{
		bools.pack(stream);
	}

	@Override
	public void unpack(InputStream stream) throws IOException, PrismException
	{
		bools.unpack(stream);
	}

	@Override
	public void appendTo(Heap_out h)
	{
		bools.appendTo(h);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		bools.extractFrom(h);
	}
}
