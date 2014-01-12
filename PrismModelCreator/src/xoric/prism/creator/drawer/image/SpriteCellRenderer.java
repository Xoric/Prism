package xoric.prism.creator.drawer.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

class SpriteCellRenderer extends JPanel implements ListCellRenderer<SpriteCell>
{
	private static final long serialVersionUID = 1L;

	private JLabel iconLabel;
	private JLabel nameLabel;
	private JLabel fileLabel;

	public SpriteCellRenderer()
	{
		super(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());
		setOpaque(true);

		iconLabel = new JLabel();
		nameLabel = new JLabel();
		fileLabel = new JLabel();

		fileLabel.setFont(fileLabel.getFont().deriveFont(9.0f));

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
				2, 2, 0, 2), 0, 0);
		add(iconLabel, c);

		c = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0);
		add(nameLabel, c);

		c = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0);
		add(fileLabel, c);
	}

	@Override
	public Component getListCellRendererComponent(JList list, SpriteCell cell, int index, boolean isSelected, boolean hasFocus)
	{
		iconLabel.setIcon(cell.getIcon());
		nameLabel.setText(cell.getFrameName());
		fileLabel.setText(cell.getFileName());

		setBackground(isSelected ? Color.lightGray : list.getBackground());

		return this;
	}
}
