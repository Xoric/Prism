package xoric.prism.swing.input;

import javax.swing.JOptionPane;

public class BooleanInputPanel extends ValueInputPanel
{
	private static final long serialVersionUID = 1L;

	private boolean value;

	public BooleanInputPanel(String name, IValueInputListener listener)
	{
		super(name, listener);

		setValue(false);
	}

	public void setValue(boolean value)
	{
		this.value = value;
		valueChanged(false);
	}

	public boolean getValue()
	{
		return value;
	}

	@Override
	protected void requestEdit()
	{
		String[] options = new String[] { "True", "False", "Cancel" };
		int i = JOptionPane.showOptionDialog(null, prompt, "Input", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[value ? 0 : 1]);

		if (i == 0 || i == 1)
		{
			value = i == 0;
			valueChanged(true);
		}
	}

	@Override
	protected String getValueText()
	{
		return value ? "True" : "False";
	}
}
