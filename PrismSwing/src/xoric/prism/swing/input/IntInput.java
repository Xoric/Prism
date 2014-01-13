package xoric.prism.swing.input;

import javax.swing.JOptionPane;

import xoric.prism.data.types.Text;

public class IntInput extends ValueInput implements IIntInput
{
	private static final long serialVersionUID = 1L;

	private int value;

	public IntInput(String name, IValueInputListener listener)
	{
		super(name, listener);
	}

	@Override
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

	@Override
	public int getValue()
	{
		return value;
	}
}
