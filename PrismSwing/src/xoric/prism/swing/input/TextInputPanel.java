package xoric.prism.swing.input;

import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;

public class TextInputPanel extends ValueInputPanel
{
	private static final long serialVersionUID = 1L;

	private Text value;

	public TextInputPanel(String name, IValueInputListener listener)
	{
		super(name, listener);

		setValue(new Text());
	}

	public void setValue(IText_r value)
	{
		this.value = new Text(value);
		valueChanged(false);
	}

	public IText_r getValue()
	{
		return value;
	}

	@Override
	protected void requestEdit()
	{
		Text t = PrismTextDialog.getInstance().showDialog("Input", prompt, value.toString());

		if (t != null && t.length() > 0)
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
