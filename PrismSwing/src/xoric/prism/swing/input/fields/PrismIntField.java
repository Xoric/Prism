package xoric.prism.swing.input.fields;

import javax.swing.text.AbstractDocument;

public class PrismIntField extends PrismField implements IValueController
{
	private static final long serialVersionUID = 1L;

	private int min;
	private boolean hasMin;
	private int max;
	private boolean hasMax;

	public PrismIntField(int i)
	{
		super(String.valueOf(i));

		hasMin = false;
		hasMax = false;

		filter = new PrismIntFilter(this);
		((AbstractDocument) (this.getDocument())).setDocumentFilter(filter);
	}

	public void setMaxValue(int max)
	{
		this.max = max;
		hasMax = true;
	}

	public void setMinValue(int min)
	{
		this.min = min;
		hasMin = true;
	}

	public int getInt()
	{
		String s = getText();
		int i;

		if (s.equals("-"))
			i = 0;
		else
			i = s.length() == 0 ? 0 : Integer.valueOf(s);
		return i;
	}

	@Override
	public void controlValue()
	{
		int i = getInt();
		int j = i;

		if (hasMin && i < min)
			i = min;
		if (hasMax && i > max)
			i = max;

		if (i != j)
			setText(String.valueOf(i));
	}
}
