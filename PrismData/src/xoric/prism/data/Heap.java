package xoric.prism.data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Heap
{
	public final List<Integer> ints;
	public final List<Float> floats;
	public final List<Text> texts;

	public Heap()
	{
		ints = new ArrayList<Integer>(5);
		floats = new ArrayList<Float>(5);
		texts = new ArrayList<Text>(5);
	}

	public Heap(int intCount, int floatCount, int textCount)
	{
		ints = intCount > 0 ? new ArrayList<Integer>(intCount) : null;
		floats = floatCount > 0 ? new ArrayList<Float>(floatCount) : null;
		texts = textCount > 0 ? new ArrayList<Text>(textCount) : null;
	}

	public Heap(List<Integer> ints, List<Float> floats, List<Text> texts)
	{
		this.ints = ints;
		this.floats = floats;
		this.texts = texts;
	}

	public void clear()
	{
		ints.clear();
		floats.clear();
		texts.clear();
	}

	public int getIntCount()
	{
		return ints != null ? ints.size() : 0;
	}

	public int getFloatCount()
	{
		return floats != null ? floats.size() : 0;
	}

	public int getTextCount()
	{
		return texts != null ? texts.size() : 0;
	}

	@Override
	public String toString()
	{
		// append ints
		StringBuffer sb = new StringBuffer();
		if (ints.size() > 0)
		{
			sb.append("i:");
			for (int i = 0; i < ints.size(); ++i)
				sb.append((i > 0 ? "," : "") + ints.get(i));
		}

		// append floats
		DecimalFormat format = new DecimalFormat("#0.00");
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		if (floats.size() > 0)
		{
			sb.append((sb.length() > 0 ? " " : "") + "f:");
			for (int i = 0; i < floats.size(); ++i)
				sb.append((i > 0 ? "," : "") + format.format(floats.get(i)));
		}

		// append texts
		if (texts.size() > 0)
		{
			sb.append((sb.length() > 0 ? " " : "") + "T:");
			for (int i = 0; i < texts.size(); ++i)
				sb.append((i > 0 ? "," : "") + "\"" + texts.get(i).cut(8) + "\"");
		}

		// check if empty
		if (sb.length() == 0)
			sb.append("empty");

		return sb.toString();
	}
}
