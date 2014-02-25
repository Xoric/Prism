package xoric.prism.creator.windows.view.tree;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import xoric.prism.swing.input.RequestFocusListener;
import xoric.prism.swing.input.fields.PrismFloatField;
import xoric.prism.swing.input.fields.PrismIntField;
import xoric.prism.ui.Ruler;

public class PrismRulerDialog
{
	private static PrismRulerDialog instance;

	private final JLabel nameLabel;
	private final PrismIntField constantField;
	private final PrismFloatField factorField;
	private final Object[] message;

	private PrismRulerDialog()
	{
		nameLabel = new JLabel();

		constantField = new PrismIntField(0);
		constantField.addAncestorListener(new RequestFocusListener());
		JPanel p1 = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 0, 0), 0, 0);
		p1.add(new JLabel("constant"), c);
		c.gridx++;
		p1.add(constantField, c);

		factorField = new PrismFloatField(0.0f);
		JPanel p2 = new JPanel(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		p2.add(new JLabel("factor"), c);
		c.gridx++;
		p2.add(factorField, c);

		message = new Object[] { null, null, nameLabel, p1, p2 };
	}

	public static PrismRulerDialog getInstance()
	{
		if (instance == null)
			instance = new PrismRulerDialog();

		return instance;
	}

	public Ruler showDialog(String title, String prompt, Ruler defaultValues)
	{
		nameLabel.setText(prompt);
		constantField.setText(String.valueOf((int) defaultValues.constant));
		constantField.addAncestorListener(new RequestFocusListener());
		factorField.setText(String.valueOf(defaultValues.factor));

		int n = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION);
		boolean b = n == JOptionPane.OK_OPTION;
		//
		Ruler r;

		if (b)
		{
			r = new Ruler();
			r.constant = constantField.getInt();
			r.factor = factorField.getFloat();
		}
		else
			r = null;

		return r;
	}
}
