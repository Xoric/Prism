package xoric.prism.creator.custom.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import xoric.prism.creator.common.view.INewDialog;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.PathInput;
import xoric.prism.swing.input.RequestFocusListener;
import xoric.prism.swing.input.fields.PrismTextField;
import xoric.prism.swing.tooltips.ToolTipFormatter;

public class NewCollectionDialog implements ActionListener, INewDialog
{
	private final JLabel nameLabel;
	private final PrismTextField nameField;

	private final JPanel pathPanel0;
	private final JTextField pathField;
	private final JPanel pathPanel;
	private final JButton pathButton;

	private final Object[] message;

	public NewCollectionDialog()
	{
		// model name
		nameLabel = new JLabel("Name");
		nameField = new PrismTextField("NEW COLLECTION");
		nameField.addAncestorListener(new RequestFocusListener());

		// working directory
		JLabel pathLabel = new JLabel("Working directory");
		JLabel pathLabel2 = new JLabel(" ? ");
		pathLabel2.setBorder(BorderFactory.createEtchedBorder());
		pathLabel2.setToolTipText(ToolTipFormatter.split("This directory will be used to store the model's settings and images."));
		pathPanel0 = new JPanel(new BorderLayout());
		pathPanel0.add(BorderLayout.CENTER, pathLabel);
		pathPanel0.add(BorderLayout.EAST, pathLabel2);
		pathPanel = new JPanel(new BorderLayout());
		pathField = new JTextField("");
		pathButton = new JButton("...");
		pathButton.addActionListener(this);
		pathPanel.add(BorderLayout.CENTER, pathField);
		pathPanel.add(BorderLayout.EAST, pathButton);

		message = new Object[] { null, null, nameLabel, nameField, pathPanel0, pathPanel };
	}

	@Override
	public NewCollectionData getResult()
	{
		Text name = new Text(nameField.getText());
		Path path = new Path(pathField.getText());

		NewCollectionData d = new NewCollectionData(name, path);

		return d;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == pathButton)
		{
			Path path = PathInput.showDialog("Select working directory");
			if (path != null)
				pathField.setText(path.toString());
		}
	}

	@Override
	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "New Collection", JOptionPane.OK_CANCEL_OPTION);
		return n == JOptionPane.OK_OPTION;
	}
}
