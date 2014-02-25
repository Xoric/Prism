package xoric.prism.creator.windows.view.tree;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import xoric.prism.ui.button.ButtonActionMask;

/**
 * @author XoricLee
 * @since 18.02.2014, 09:43:39
 */
class ActionMaskDialog
{
	private final JTextField intSources;
	private final JTextField textSources;
	private final Object[] message;

	public ActionMaskDialog(ButtonActionMask mask)
	{
		message = new Object[6];
		int n = 2;

		message[n++] = new JLabel("List indices of components that should be read when executing the button's action.");
		message[n++] = new JLabel("Separate indices by comma (e.g. 2,3,0).");

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(BorderLayout.WEST, new JLabel("int sources: "));
		p1.add(BorderLayout.CENTER, intSources = new JTextField());
		message[n++] = p1;

		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(BorderLayout.WEST, new JLabel("text sources: "));
		p2.add(BorderLayout.CENTER, textSources = new JTextField());
		message[n++] = p2;

		if (mask != null)
		{
			fillField(intSources, mask.getIntSources());
			fillField(textSources, mask.getTextSources());
		}
	}

	private void fillField(JTextField f, List<Integer> list)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); ++i)
		{
			if (i > 0)
				sb.append(",");

			sb.append(list.get(i));
		}
		f.setText(sb.toString());
	}

	private List<Integer> extractInts(String line)
	{
		List<Integer> list = new ArrayList<Integer>();

		if (line.length() > 0)
		{
			String[] s = line.split(",");

			for (int i = 0; i < s.length; ++i)
			{
				int v = Integer.valueOf(s[i]);
				list.add(v);
			}
		}
		return list;
	}

	public List<Integer> getIntSources()
	{
		List<Integer> intSources;
		try
		{
			intSources = extractInts(this.intSources.getText());
		}
		catch (Exception e)
		{
			intSources = null;
			JOptionPane.showMessageDialog(null, "Input was invalid.", "Setup action mask", JOptionPane.WARNING_MESSAGE);
		}
		return intSources;
	}

	public List<Integer> getTextSources()
	{
		List<Integer> textSources;
		try
		{
			textSources = extractInts(this.textSources.getText());
		}
		catch (Exception e)
		{
			textSources = null;
			JOptionPane.showMessageDialog(null, "Input was invalid.", "Setup action mask", JOptionPane.WARNING_MESSAGE);
		}
		return textSources;
	}

	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "Setup action mask", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}
}
