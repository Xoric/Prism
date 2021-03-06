package xoric.prism.swing.input;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

public class PointInputPanel extends ValueInputPanel
{
	private static final long serialVersionUID = 1L;

	private Point value;

	private String xLabel;
	private String yLabel;

	public PointInputPanel(String name, IValueInputListener listener)
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

	public static Point showInputMessage(String title, String prompt, String xName, String yName, int defaultX, int defaultY)
	{
		JTextField XField = new JTextField(String.valueOf(defaultX));
		JTextField yField = new JTextField(String.valueOf(defaultY));

		Object[] message = { prompt, null, new JLabel(xName), XField, new JLabel(yName), yField };

		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		pane.createDialog(null, title).setVisible(true);

		int x = castInt(XField.getText());
		int y = castInt(yField.getText());

		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;

		return new Point(x, y);
	}

	@Override
	protected void requestEdit()
	{
		Point p = showInputMessage(name, prompt, xLabel, yLabel, value.x, value.y);

		if (p != null)
		{
			value.copyFrom(p);
			valueChanged(true);
		}
	}

	@Override
	protected String getValueText()
	{
		return value.x + " x " + value.y;
	}
}
