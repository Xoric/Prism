package xoric.prism.creator.windows.view.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import xoric.prism.client.ui.ClientActionCommand;
import xoric.prism.data.types.IText_r;

/**
 * @author XoricLee
 * @since 18.02.2014, 09:43:39
 */
class ActionIndexDialog
{
	private final List<JRadioButton> radioButtons;
	private final Object[] message;

	public ActionIndexDialog(IText_r actionCommand)
	{
		radioButtons = new ArrayList<JRadioButton>();
		ButtonGroup group = new ButtonGroup();
		message = new Object[2 + ClientActionCommand.COUNT];

		for (int i = 0; i < ClientActionCommand.COUNT; ++i)
		{
			ClientActionCommand a = ClientActionCommand.VALUES[i];
			JRadioButton b = new JRadioButton(a.toString());
			b.setSelected(a.toString().equals(actionCommand.toString()));
			group.add(b);
			radioButtons.add(b);
			message[2 + i] = b;
		}
	}

	public ClientActionCommand getResult()
	{
		ClientActionCommand a = ClientActionCommand.NONE;

		for (int i = 0; i < radioButtons.size(); ++i)
			if (radioButtons.get(i).isSelected())
				a = ClientActionCommand.VALUES[i];

		return a;
	}

	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "Select action command", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}
}
