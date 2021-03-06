package xoric.prism.swing.input;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import xoric.prism.data.types.Text;
import xoric.prism.swing.input.fields.PrismTextField;

public class PrismTextDialog
{
	private static PrismTextDialog instance;

	private final JLabel nameLabel;
	private final PrismTextField nameField;
	private final Object[] message;

	private PrismTextDialog()
	{
		nameLabel = new JLabel();
		nameField = new PrismTextField("");

		message = new Object[] { null, null, nameLabel, nameField };
	}

	public static PrismTextDialog getInstance()
	{
		if (instance == null)
			instance = new PrismTextDialog();

		return instance;
	}

	public Text showDialog(String title, String prompt, String defaultText)
	{
		nameLabel.setText(prompt);
		nameField.setText(new Text(defaultText).toString());
		nameField.addAncestorListener(new RequestFocusListener());

		int n = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION);
		boolean b = n == JOptionPane.OK_OPTION;
		//
		Text t = b ? new Text(nameField.getText()) : null;

		return t;
	}
}
