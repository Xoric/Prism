package xoric.prism.creator.models.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.SwingConstants;

import xoric.prism.creator.common.view.INewDialog;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.PathInputDialog;
import xoric.prism.swing.input.RequestFocusListener;
import xoric.prism.swing.input.fields.IInputListener;
import xoric.prism.swing.input.fields.PrismIntField;
import xoric.prism.swing.input.fields.PrismTextField;
import xoric.prism.swing.tooltips.ToolTipFormatter;

public class NewModelDialog implements ActionListener, IInputListener, INewDialog
{
	private final JLabel nameLabel;
	private final PrismTextField nameField;

	private final JLabel sizeLabel;
	private final PrismIntField widthField;
	private final PrismIntField heightField;
	private final JPanel sizePanel;

	private final JPanel pathPanel0;
	private final JTextField pathField;
	private final JPanel pathPanel;
	private final JButton pathButton;

	private final JLabel previewLabel;
	private final JLabel previewWidthLabel;
	private final JLabel previewHeightLabel;
	private final JPanel innerPreviewPanel;
	private final JPanel previewPanel;

	//	private final JOptionPane pane;
	private final Object[] message;

	public NewModelDialog()
	{
		// maximum tile size
		final int previewSizeMaxX = 384;
		final int previewSizeMaxY = 256;

		final int tileSizeMaxX = 256;
		final int tileSizeMaxY = 200;

		// model name
		nameLabel = new JLabel("Name");
		nameField = new PrismTextField("NEW MODEL");
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

		// tile size
		final int tileSizeX = 48;
		final int tileSizeY = 48;
		sizeLabel = new JLabel("Tile size");
		widthField = new PrismIntField(tileSizeX);
		heightField = new PrismIntField(tileSizeY);
		widthField.setMaxValue(tileSizeMaxX);
		widthField.setMinValue(0);
		heightField.setMaxValue(tileSizeMaxY);
		widthField.setMinValue(0);
		widthField.setInputListener(this);
		heightField.setInputListener(this);
		sizePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.35, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		sizePanel.add(widthField, c);
		c = new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		JLabel l = new JLabel("x");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		sizePanel.add(l, c);
		c = new GridBagConstraints(2, 0, 1, 1, 0.35, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
				0);
		sizePanel.add(heightField, c);
		c = new GridBagConstraints(3, 0, 1, 1, 0.2, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		l = new JLabel("pixels");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		sizePanel.add(l, c);
		JLabel sizeLabel2 = new JLabel(" ? ");
		sizeLabel2.setBorder(BorderFactory.createEtchedBorder());
		sizeLabel2.setToolTipText(ToolTipFormatter.split("Size of one sprite within the images."));
		c = new GridBagConstraints(4, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		l.setHorizontalAlignment(SwingConstants.CENTER);
		sizePanel.add(sizeLabel2, c);

		// preview label
		previewLabel = new JLabel();
		previewLabel.setBackground(new Color(0, 0, 0, 125));
		previewLabel.setOpaque(true);
		previewLabel.setBorder(BorderFactory.createLineBorder(Color.black));

		previewWidthLabel = new JLabel();
		previewHeightLabel = new JLabel();

		innerPreviewPanel = new JPanel(new BorderLayout());
		innerPreviewPanel.add(BorderLayout.CENTER, previewLabel);
		innerPreviewPanel.add(BorderLayout.EAST, previewHeightLabel);
		innerPreviewPanel.add(BorderLayout.SOUTH, previewWidthLabel);

		previewPanel = new JPanel(new GridBagLayout());
		c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		previewPanel.add(innerPreviewPanel, c);
		previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
		previewPanel.setPreferredSize(new Dimension(previewSizeMaxX, previewSizeMaxY));

		message = new Object[] { null, null, nameLabel, nameField, pathPanel0, pathPanel, sizeLabel, sizePanel, previewPanel };

		setPreview(tileSizeX, tileSizeY);
	}

	@Override
	public NewModelData getResult()
	{
		Text name = new Text(nameField.getText());
		Path path = new Path(pathField.getText());
		int x = widthField.getInt();
		int y = heightField.getInt();
		Point tileSize = new Point(x, y);

		NewModelData d = new NewModelData(name, path, tileSize);

		return d;
	}

	private void setPreview(int x, int y)
	{
		previewWidthLabel.setText("width: " + x);
		previewHeightLabel.setText(" height: " + y);
		Dimension d = new Dimension(x, y);
		previewLabel.setMinimumSize(d);
		previewLabel.setMaximumSize(d);
		previewLabel.setPreferredSize(d);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == pathButton)
		{
			Path path = PathInputDialog.showDialog("Select working directory");
			if (path != null)
				pathField.setText(path.toString());
		}
	}

	@Override
	public void changedInput(Object source)
	{
		if (source == widthField || source == heightField)
		{
			setPreview(widthField.getInt(), heightField.getInt());
		}
	}

	@Override
	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "New Model", JOptionPane.OK_CANCEL_OPTION);
		return n == JOptionPane.OK_OPTION;
	}
}
