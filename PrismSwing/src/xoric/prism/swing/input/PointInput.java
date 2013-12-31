package xoric.prism.swing.input;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import xoric.prism.data.Point;

public class PointInput extends ValueInput
{
	private static final long serialVersionUID = 1L;

	private Point value;

	private String xLabel;
	private String yLabel;

	public PointInput(String name)
	{
		super(name);

		this.xLabel = "X";
		this.yLabel = "Y";

		setValue(new Point());
	}

	public void setLabels(String xLabel, String yLabel)
	{
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}

	public void setValue(Point value)
	{
		this.value = value;
		updateValueDisplay();
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

		if (w > 0 && h > 0)
		{
			value.x = w;
			value.y = h;
			updateValueDisplay();
		}
	}

	@Override
	protected String getValueText()
	{
		return value.x + " x " + value.y;
	}
}
