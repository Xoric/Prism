package xoric.prism.swing.input;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import xoric.prism.data.types.Path;

public class OpenPathDialog implements ActionListener
{
	private final JPanel pathPanel0;
	private final JTextField pathField;
	private final JPanel pathPanel;
	private final JButton pathButton;

	private final Object[] message;

	private final String title;

	public OpenPathDialog(String title, String caption)
	{
		this.title = title;

		// working directory
		JLabel pathLabel = new JLabel(caption);
		pathPanel0 = new JPanel(new BorderLayout());
		pathPanel0.add(BorderLayout.CENTER, pathLabel);
		pathPanel = new JPanel(new BorderLayout());
		pathField = new JTextField("");
		pathButton = new JButton("...");
		pathButton.addActionListener(this);
		pathPanel.add(BorderLayout.CENTER, pathField);
		pathPanel.add(BorderLayout.EAST, pathButton);

		pathField.addAncestorListener(new RequestFocusListener());

		message = new Object[] { null, null, pathPanel0, pathPanel };
	}

	public Path getResult()
	{
		Path path = new Path(pathField.getText());
		return path;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == pathButton)
		{
			Path path = PathInputDialog.showDialog(title);
			if (path != null)
				pathField.setText(path.toString());
		}
	}

	public boolean showOpenPathDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}
}
