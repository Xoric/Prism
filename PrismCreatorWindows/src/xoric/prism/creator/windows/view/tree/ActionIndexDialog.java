package xoric.prism.creator.windows.view.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import xoric.prism.client.ui.button.ButtonActionIndex;

/**
 * @author XoricLee
 * @since 18.02.2014, 09:43:39
 */
class ActionIndexDialog
{
	private final List<JRadioButton> radioButtons;
	private final Object[] message;

	public ActionIndexDialog(ButtonActionIndex actionIndex)
	{
		radioButtons = new ArrayList<JRadioButton>();
		ButtonGroup group = new ButtonGroup();
		message = new Object[2 + ButtonActionIndex.COUNT];

		for (int i = 0; i < ButtonActionIndex.COUNT; ++i)
		{
			ButtonActionIndex a = ButtonActionIndex.VALUES[i];
			JRadioButton b = new JRadioButton(a.toString());
			b.setSelected(a == actionIndex);
			group.add(b);
			radioButtons.add(b);
			message[2 + i] = b;
		}
	}

	public ButtonActionIndex getResult()
	{
		ButtonActionIndex a = ButtonActionIndex.NONE;

		for (int i = 0; i < radioButtons.size(); ++i)
			if (radioButtons.get(i).isSelected())
				a = ButtonActionIndex.VALUES[i];

		return a;
	}

	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "Select action index", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}
}
