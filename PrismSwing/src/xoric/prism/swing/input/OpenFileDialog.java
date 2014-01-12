package xoric.prism.swing.input;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OpenFileDialog implements ActionListener
{
	private final JPanel filePanel0;
	private final JTextField fileField;
	private final JPanel filePanel;
	private final JButton fileButton;

	private final Object[] message;

	private final String title;

	public OpenFileDialog(String title, String caption)
	{
		this.title = title;

		// working directory
		JLabel pathLabel = new JLabel(caption);
		filePanel0 = new JPanel(new BorderLayout());
		filePanel0.add(BorderLayout.CENTER, pathLabel);
		filePanel = new JPanel(new BorderLayout());
		fileField = new JTextField("");
		fileButton = new JButton("...");
		fileButton.addActionListener(this);
		filePanel.add(BorderLayout.CENTER, fileField);
		filePanel.add(BorderLayout.EAST, fileButton);

		message = new Object[] { null, null, filePanel0, filePanel };
	}

	public File getResult()
	{
		File file = new File(fileField.getText());

		return file;
	}

	public boolean show()
	{
		int n = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == fileButton)
		{
			JFileChooser c = new JFileChooser();
			int i = c.showOpenDialog(null);

			if (i == JFileChooser.APPROVE_OPTION)
				fileField.setText(c.getSelectedFile().toString());
		}
	}

	public void setDefaultFile(File file)
	{
		fileField.setText(file == null ? "" : file.toString());
	}
}
