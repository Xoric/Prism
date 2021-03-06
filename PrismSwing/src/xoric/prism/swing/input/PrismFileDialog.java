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
import javax.swing.filechooser.FileFilter;

public class PrismFileDialog implements ActionListener
{
	private final JFileChooser fileChooser;
	private boolean isSaveDialog;
	private String[] options;

	private final JPanel filePanel0;
	private final JTextField fileField;
	private final JPanel filePanel;
	private final JButton fileButton;

	private final Object[] message;

	private final String title;

	public PrismFileDialog(String title, String caption)
	{
		this.title = title;
		this.fileChooser = new JFileChooser();
		this.isSaveDialog = false;
		this.options = new String[2];
		this.options[1] = "Cancel";

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

		fileField.addAncestorListener(new RequestFocusListener());

		message = new Object[] { null, null, filePanel0, filePanel };
	}

	public File getResult()
	{
		return new File(fileField.getText());
	}

	public void setFilter(FileFilter filter)
	{
		fileChooser.setFileFilter(filter);
	}

	public boolean showSaveDialog(String buttonCaption)
	{
		this.isSaveDialog = true;
		return showDialog(buttonCaption);
	}

	public boolean showOpenDialog(String buttonCaption)
	{
		this.isSaveDialog = false;
		return showDialog(buttonCaption);
	}

	private boolean showDialog(String buttonCaption)
	{
		options[0] = buttonCaption;

		int n = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				options, options[0]);

		return n == 0;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == fileButton)
		{
			JFileChooser c = new JFileChooser();
			int i = isSaveDialog ? c.showSaveDialog(null) : c.showOpenDialog(null);

			if (i == JFileChooser.APPROVE_OPTION)
				fileField.setText(c.getSelectedFile().toString());
		}
	}

	public void setDefaultFile(File file)
	{
		fileField.setText(file == null ? "" : file.toString());
	}
}
