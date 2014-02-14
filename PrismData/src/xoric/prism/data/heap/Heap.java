package xoric.prism.data.heap;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xoric.prism.data.types.Text;

public class Heap
{
	public final List<Integer> ints;
	public final List<Float> floats;
	public final List<Text> texts;

	private int intIndex;
	private int floatIndex;
	private int textIndex;

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

	public void rewind()
	{
		intIndex = 0;
		floatIndex = 0;
		textIndex = 0;
	}

	public int nextInt()
	{
		return ints.get(intIndex++);
	}

	public float nextFloat()
	{
		return floats.get(floatIndex++);
	}

	public Text nextText()
	{
		return texts.get(textIndex++);
	}

	public void clear()
	{
		ints.clear();
		floats.clear();
		texts.clear();
	}

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
		if (texts != null && texts.size() > 0)
		{
			sb.append((sb.length() > 0 ? " " : "") + "T:");
			for (int i = 0; i < texts.size(); ++i)
				sb.append((i > 0 ? "," : "") + "\"" + texts.get(i).cut(8) + "\"");
		}
		sb.append("]");

		return sb.toString();
	}
}
