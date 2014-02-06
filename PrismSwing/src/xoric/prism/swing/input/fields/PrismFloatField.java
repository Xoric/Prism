package xoric.prism.swing.input.fields;

import javax.swing.text.AbstractDocument;

public class PrismFloatField extends PrismField implements IValueController
{
	private static final long serialVersionUID = 1L;

	private float min;
	private boolean hasMin;
	private float max;
	private boolean hasMax;

	public PrismFloatField(float f)
	{
		super(String.valueOf(f));

		hasMin = false;
		hasMax = false;

		filter = new PrismFloatFilter(this);
		((AbstractDocument) (this.getDocument())).setDocumentFilter(filter);
	}

	public void setMaxValue(float max)
	{
		hasMax = true;
		this.max = max;
	}

	public void setMinValue(float min)
	{
		hasMin = true;
		this.min = min;
	}

	public float getFloat()
	{
		String s = getText();
		float f = s.length() == 0 ? 0 : Float.valueOf(s);
		return f;
	}

	@Override
	public void controlValue()
	{
		float f = getFloat();

		if (hasMin && f < min)
		{
			f = min;
			setText(String.valueOf(f));
		}
		else if (hasMax && f > max)
		{
			f = max;
			setText(String.valueOf(f));
		}
	}
}
