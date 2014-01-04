package xoric.prism.swing.input;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

public class PointInput extends ValueInput
{
	private static final long serialVersionUID = 1L;

	private Point value;

	private String xLabel;
	private String yLabel;

	public PointInput(String name, IValueInputListener listener)
	{
		super(name, listener);

		this.xLabel = "X";
		this.yLabel = "Y";

		this.value = new Point();
		setValue(this.value);
	}

	public void setLabels(String xLabel, String yLabel)
	{
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}

	public void setValue(IPoint_r value)
	{
		this.value.copyFrom(value);
		valueChanged(false);
	}

	public IPoint_r getValue()
	{
		return value;
	}

	private static int castInt(String s)
	{
		int i;
		try
		{
			i = Integer.valueOf(s);
		}
		catch (Exception e)
		{
			i = 0;
		}
		return i;
	}

	@Override
	protected void requestEdit()
	{
		JTextField widthField = new JTextField(String.valueOf(value.x));
		JTextField heightField = new JTextField(String.valueOf(value.y));

		Object[] message = { prompt, null, xLabel, widthField, yLabel, heightField };

		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		pane.createDialog(null, name).setVisible(true);

		int w = castInt(widthField.getText());
		int h = castInt(heightField.getText());

		if (w < 0)
			w = 0;
		if (h < 0)
			h = 0;

		value.x = w;
		value.y = h;
		valueChanged(true);
	}

	@Override
	protected String getValueText()
	{
		return value.x + " x " + value.y;
	}
}