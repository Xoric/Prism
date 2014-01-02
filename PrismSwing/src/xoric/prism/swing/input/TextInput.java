package xoric.prism.swing.input;

import javax.swing.JOptionPane;

import xoric.prism.data.types.Text;

public class TextInput extends ValueInput
{
	private static final long serialVersionUID = 1L;

	private Text value;

	public TextInput(String name, IValueInputListener listener)
	{
		super(name, listener);

		setValue(new Text());
	}

	public void setValue(Text value)
	{
		this.value = value;
		valueChanged(false);
	}

	@Override
	protected void requestEdit()
	{
		String s = JOptionPane.showInputDialog(null, prompt, value.toString());
		Text t = new Text();
		if (s != null)
			t.set(s);

		if (t.length() > 0)
		{
			value = t;
			valueChanged(true);
		}
	}

	@Override
	protected String getValueText()
	{
		return value.toString();
	}
}
