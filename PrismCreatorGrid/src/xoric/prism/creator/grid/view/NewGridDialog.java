package xoric.prism.creator.grid.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.input.PathInput;
import xoric.prism.swing.input.RequestFocusListener;
import xoric.prism.swing.input.ValueInput;
import xoric.prism.swing.input.fields.PrismIntField;
import xoric.prism.swing.input.fields.PrismTextField;
import xoric.prism.swing.tooltips.ToolTipFormatter;

public class NewGridDialog implements ActionListener, INewDialog, IValueInputListener
{
	private final JLabel nameLabel;
	private final PrismTextField nameField;

	private final JLabel sizeLabel;
	private final PrismIntField widthField;
	private final PrismIntField heightField;

	private final JPanel pathPanel0;
	private final JTextField pathField;
	private final JPanel pathPanel;
	private final JButton pathButton;

	private final Object[] message;

	public NewGridDialog()
	{
		// model name
		nameLabel = new JLabel("Name");
		nameField = new PrismTextField("NEW GRID");
		nameField.addAncestorListener(new RequestFocusListener());

		// sprite size
		sizeLabel = new JLabel("Sprite size");
		widthField = new PrismIntField(32);
		heightField = new PrismIntField(32);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0);
		JPanel sizePanel = new JPanel(new GridBagLayout());
		sizePanel.add(widthField, c);
		c.gridx++;
		sizePanel.add(heightField, c);

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

		message = new Object[] { null, null, nameLabel, nameField, sizeLabel, sizePanel, pathPanel0, pathPanel };
	}

	@Override
	public NewGridData getResult()
	{
		Text name = new Text(nameField.getText());
		Point spriteSize = new Point(widthField.getInt(), heightField.getInt());
		Path path = new Path(pathField.getText());

		NewGridData d = new NewGridData(name, spriteSize, path);

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

	@Override
	public void notifyValueChanged(ValueInput input)
	{
	}
}
