package xoric.prism.creator.windows.view.tree;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import xoric.prism.client.ui.edit.InputFormat;
import xoric.prism.swing.input.fields.PrismIntField;

/**
 * @author XoricLee
 * @since 18.02.2014, 09:43:39
 */
class InputFormatDialog
{
	private final List<JRadioButton> radioButtons;
	private final PrismIntField maxLengthField;
	private final Object[] message;

	public InputFormatDialog(InputFormat format, int maxLength)
	{
		radioButtons = new ArrayList<JRadioButton>();
		ButtonGroup group = new ButtonGroup();
		message = new Object[3 + InputFormat.COUNT];

		for (int i = 0; i < InputFormat.COUNT; ++i)
		{
			InputFormat f = InputFormat.VALUES[i];
			JRadioButton b = new JRadioButton(f.toString());
			b.setSelected(f == format);
			group.add(b);
			radioButtons.add(b);
			message[2 + i] = b;
		}

		JPanel p = new JPanel(new BorderLayout());
		p.add(BorderLayout.WEST, new JLabel("max length: "));
		p.add(BorderLayout.CENTER, maxLengthField = new PrismIntField(maxLength));
		message[message.length - 1] = p;
	}

	public InputFormatResult getResult()
	{
		InputFormat f = InputFormat.NORMAL;

		for (int i = 0; i < radioButtons.size(); ++i)
			if (radioButtons.get(i).isSelected())
				f = InputFormat.VALUES[i];

		int maxLength = maxLengthField.getInt();

		InputFormatResult r = new InputFormatResult(f, maxLength);

		return r;
	}

	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "Select input format", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}
}
