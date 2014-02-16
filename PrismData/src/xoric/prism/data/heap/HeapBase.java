package xoric.prism.data.heap;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xoric.prism.data.types.IText_r;

public abstract class HeapBase
{
	public final List<Integer> ints;
	public final List<Float> floats;

	public HeapBase()
	{
		ints = new ArrayList<Integer>(5);
		floats = new ArrayList<Float>(5);
	}

	public HeapBase(int intCount, int floatCount)
	{
		ints = new ArrayList<Integer>(intCount);
		floats = new ArrayList<Float>(floatCount);
	}

	public void clear()
	{
		ints.clear();
		floats.clear();
	}

	public abstract IText_r getText(int index);

	public abstract int getTextCount();

	@Override
	public String toString()
	{
		// append ints
		StringBuffer sb = new StringBuffer("[");
		if (ints != null && ints.size() > 0)
		{
			sb.append("i:");
			for (int i = 0; i < ints.size(); ++i)
				sb.append((i > 0 ? "," : "") + ints.get(i));
		}

		// append floats
		DecimalFormat format = new DecimalFormat("#0.00");
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		if (floats != null && floats.size() > 0)
		{
			sb.append((sb.length() > 0 ? " " : "") + "f:");
			for (int i = 0; i < floats.size(); ++i)
				sb.append((i > 0 ? "," : "") + format.format(floats.get(i)));
		}

		// append texts
		int n = getTextCount();
		if (n > 0)
		{
			sb.append((sb.length() > 0 ? " " : "") + "T:");
			for (int i = 0; i < n; ++i)
				sb.append((i > 0 ? "," : "") + "\"" + getText(i).cut(8) + "\"");
		}
		sb.append("]");

		return sb.toString();
	}
}
