package xoric.prism.data.physics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.heap.IStackable;
import xoric.prism.data.packable.IPackable;
import xoric.prism.data.packable.IntPacker;
import xoric.prism.data.types.FloatPoint;
import xoric.prism.data.types.IFloatPoint_r;

/**
 * @author XoricLee
 * @since 02.01.2012, 15:56:13
 */
public class Angle implements IAngle_r, IPackable, IStackable
{
	private static final int RD = 15;
	private static final int THRES_RIGHT_MAX = 90 - RD;
	private static final int THRES_RIGHT_MIN = 270 + RD;
	private static final int THRES_TOPRIGHT_MAX = 90;
	private static final int THRES_TOPLEFT_MAX = 90 + RD;
	private static final int THRES_LEFT_MAX = 270 - RD;
	private static final int THRES_BOTTOMLEFT_MAX = 270;

	protected static final float PI = 3.1415926535897932384626433832795f;
	protected static final float PI2 = 6.283185307179586476925286766559f;
	protected static final float DEG_TO_RAD = 0.01745329251994329576923690768489f; // = pi / 180
	protected static final float RAD_TO_DEG = 57.295779513082320876798154814105f; // = 180 / pi

	protected int degree;
	protected View6 view;
	protected boolean recalcView;
	protected FloatPoint components;

	/**
	 * Readable direction constructor.
	 * @param degree
	 *            angle in degree where 0 means right, 90 means top
	 */
	public Angle(int degree)
	{
		set(degree);
	}

	/**
	 * Readable direction constructor.
	 * @param radian
	 *            direction in radian where 0 means right, {@code pi/2} means top
	 */
	public Angle(float radian)
	{
		set(radian);
	}

	/**
	 * Readable direction constructor. Initializes with zero which means facing right.
	 */
	public Angle()
	{
		set(0);
	}

	/**
	 * Sets this direction to a given value in degree.
	 * @param degree
	 *            direction in degree
	 */
	public void set(int degree)
	{
		degree = degree % 360;
		if (degree < 0)
			degree += 360;

		this.recalcView = true;
		this.degree = degree;
		this.components = null;
	}

	/**
	 * Sets this direction to a given value in radian.
	 * @param radian
	 *            direction in radian
	 */
	public void set(float radian)
	{
		int degree = (int) (RAD_TO_DEG * radian);
		set(degree);
	}

	@Override
	public View4 getView(boolean bidirectional)
	{
		return getViewInfo().getView(bidirectional);
	}

	@Override
	public View6 getViewInfo()
	{
		if (recalcView)
		{
			recalcView = false;

			if (degree < THRES_RIGHT_MAX || degree > THRES_RIGHT_MIN)
				view = View6.RIGHT;
			else if (degree < THRES_TOPRIGHT_MAX)
				view = View6.TOP_RIGHT;
			else if (degree < THRES_TOPLEFT_MAX)
				view = View6.TOP_LEFT;
			else if (degree < THRES_LEFT_MAX)
				view = View6.LEFT;
			else if (degree < THRES_BOTTOMLEFT_MAX)
				view = View6.BOTTOM_LEFT;
			else
				view = View6.BOTTOM_RIGHT;
		}
		return view;
	}

	public void addDegree(int degree)
	{
		set(this.degree + degree);
	}

	/**
	 * Returns this direction's value in degree (0 to 359) where 0 means right, 90 means top
	 * @return int
	 */
	@Override
	public int getDegree()
	{
		return degree;
	}

	/**
	 * Calculates and returns the components in x-direction and y-direction for this direction using {@code cosinus} and {@code sinus}.
	 * @return IFloatPoint_r
	 */
	@Override
	public IFloatPoint_r getComponents()
	{
		if (components == null)
		{
			float radian = DEG_TO_RAD * degree;
			components = new FloatPoint((float) Math.cos(radian), (float) -Math.sin(radian));
		}
		return components;
	}

	@Override
	public String toString()
	{
		return degree + "�";
	}

	public void set(IFloatPoint_r from, IFloatPoint_r to)
	{
		float x = to.getX() - from.getX();
		float y = to.getY() - from.getY();

		int minuend = x > 0.0f ? 360 : 180;
		int degree = minuend - (int) (RAD_TO_DEG * Math.atan(y / x));

		this.set(degree);
	}

	@Override
	public void pack(OutputStream stream) throws IOException
	{
		IntPacker.pack_s(stream, degree);
	}

	@Override
	public void unpack(InputStream stream) throws IOException
	{
		set(IntPacker.unpack_s(stream));
	}

	// IStackable_out:
	@Override
	public void appendTo(Heap_out h)
	{
		h.ints.add(degree);
	}

	// IStackable_in:
	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		set(h.nextInt());
	}
}
