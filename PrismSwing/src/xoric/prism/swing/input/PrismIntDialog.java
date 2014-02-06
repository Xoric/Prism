package xoric.prism.swing.input;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import xoric.prism.swing.input.fields.PrismIntField;

public class PrismIntDialog
{
	private static PrismIntDialog instance;

	private final JLabel nameLabel;
	private final PrismIntField valueField;
	private final Object[] message;

	private PrismIntDialog()
	{
		nameLabel = new JLabel();
		valueField = new PrismIntField(0);
		valueField.addAncestorListener(new RequestFocusListener());

		message = new Object[] { null, null, nameLabel, valueField };
	}

	public static PrismIntDialog getInstance()
	{
		if (instance == null)
			instance = new PrismIntDialog();

		return instance;
	}

	public Integer showDialog(String title, String prompt, int defaultValue)
	{
		nameLabel.setText(prompt);
		valueField.setText(String.valueOf(defaultValue));
		valueField.addAncestorListener(new RequestFocusListener());

		int n = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION);
		boolean b = n == JOptionPane.OK_OPTION;
		//
		Integer i = b ? valueField.getInt() : null;

		return i;
	}
}
