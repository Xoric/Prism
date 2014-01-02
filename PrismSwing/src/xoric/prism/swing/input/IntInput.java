package xoric.prism.swing.input;

import javax.swing.JOptionPane;

import xoric.prism.data.types.Text;

public class IntInput extends ValueInput
{
	private static final long serialVersionUID = 1L;

	private int value;

	public IntInput(String name, /*int width,*/IValueInputListener listener)
	{
		super(name, /* width,*/listener);
	}

	public void setValue(int value)
	{
		this.value = value;
		valueChanged(false);
	}

	@Override
	protected void requestEdit()
	{
		String s = JOptionPane.showInputDialog(null, prompt, String.valueOf(value));
		Text t = new Text();
		if (s != null)
			t.set(s);

		int v;
		try
		{
			v = Integer.valueOf(s);
		}
		catch (Exception e)
		{
			v = -1;
		}

		if (v >= 0)
		{
			value = v;
			valueChanged(true);
		}
	}

	@Override
	protected String getValueText()
	{
		return String.valueOf(value);
	}
}
