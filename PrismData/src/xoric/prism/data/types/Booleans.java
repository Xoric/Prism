package xoric.prism.data.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;

/**
 * @author XoricLee
 * @since 30.10.2011, 17:17:13
 */
public class Booleans implements IPackable, IStackable
{
	private static final int[] MASKS = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, -1 };
	public static final int ALL_FALSE = 0;
	public static final int ALL_TRUE = 255;

	private int values;

	public Booleans()
	{
	}

	public Booleans(Booleans other)
	{
		this.values = other.values;
	}

	public Booleans(int values)
	{
		this.values = values;
	}

	public Booleans(boolean... values)
	{
		setValues(values);
	}

	/**
	 * Returns an int representing this Booleans object.
	 * @return int
	 */
	public int toInt()
	{
		return values;
	}

	/**
	 * Copies values from a given int.
	 * @param values
	 */
	public void setValues(int values)
	{
		this.values = values;
	}

	/**
	 * Returns a boolean array of the given size containing this objects values.
	 * @param count
	 *            number between 1 and 8
	 * @return boolean[]
	 */
	public boolean[] toBools(int count)
	{
		boolean[] bools = new boolean[count];
		for (int i = 0; i < count; ++i)
			bools[i] = getValue(i);
		return bools;
	}

	/**
	 * Sets the boolean value for the given index.
	 * @param index
	 *            Index in the range of 0 to 7
	 * @param b
	 */
	public void setValue(int index, boolean b)
	{
		int mask = MASKS[index];
		if (b)
			values = values | mask;
		else
			values = values & ~mask;
	}

	/**
	 * Copies the given boolean values.
	 * @param values
	 */
	public void setValues(boolean... values)
	{
		int i = 0;
		for (boolean v : values)
			setValue(i++, v);
	}

	public void setAllValues(boolean value)
	{
		values = value ? ALL_TRUE : ALL_FALSE;
	}

	/**
	 * Returns the boolean value for the given index.
	 * @param index
	 *            Index in the range of 0 to 7
	 * @return boolean
	 */
	public boolean getValue(int index)
	{
		int mask = MASKS[index];
		return (values & mask) > 0;
	}

	/**
	 * Creates a Booleans object combining a given set of Booleans objects with the bitwise-and operator. Each boolean in the resulting
	 * object is true if and only if all of the given Booleans are true for this index.
	 * @param bools
	 * @return Booleans
	 */
	public static Booleans getBitwiseAnd(Booleans... bools)
	{
		int values = ALL_TRUE;
		for (Booleans b : bools)
			values = values & b.toInt();
		Booleans result = new Booleans(values);
		return result;
	}

	/**
	 * Creates a Booleans object combining a given set of Booleans objects with the bitwise-or operator. Each boolean in the resulting
	 * object is true if at least one of the given Booleans is true for this index.
	 * @param bools
	 * @return Booleans
	 */
	public static Booleans getBitwiseOr(Booleans... bools)
	{
		int values = ALL_FALSE;
		for (Booleans b : bools)
			values = values | b.toInt();
		Booleans result = new Booleans(values);
		return result;
	}

	/**
	 * @return Returns if any boolean in this object is true.
	 */
	public boolean anyIsTrue()
	{
		return values > 0;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 7; i >= 0; --i)
			sb.append(getValue(i) ? "1" : "0");

		return String.valueOf(values) + " (" + sb.toString() + ")";
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, values);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		values = IntPacker.unpack_s(stream);
	}

	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add(values);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		values = h.nextInt();
	}
}
