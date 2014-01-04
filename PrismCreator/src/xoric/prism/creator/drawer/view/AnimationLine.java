package xoric.prism.creator.drawer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.creator.drawer.control.IDrawerControl2;
import xoric.prism.world.entities.AnimationIndex;

public class AnimationLine extends JPanel implements ActionListener, IAnimationLine
{
	private static final long serialVersionUID = 1L;

	private final JLabel nameLabel;
	private final JLabel infoLabel;
	private final JButton directionsButton;
	private final JLabel descriptionLabel;
	private final JButton addButton;
	private final JButton deleteButton;

	private IDrawerControl2 control;

	public AnimationLine(AnimationIndex animation)
	{
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		nameLabel = createLabel(animation.toString());
		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));

		infoLabel = new JLabel();
		directionsButton = new JButton();
		descriptionLabel = createLabel(animation.getDescription().toLowerCase());
		addButton = createButton("Add", "icons/add.png", "Click to add this animation");
		deleteButton = createButton("Delete", "icons/del.png", "Click to delete this animation");

		directionsButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		showAddButton(true);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		add(nameLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
				0);
		add(descriptionLabel, c);

		c = new GridBagConstraints(1, 0, 1, 2, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		add(buttonPanel, c);

		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	}

	public void setControl(IDrawerControl2 control)
	{
		this.control = control;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		nameLabel.setEnabled(enabled);
		descriptionLabel.setEnabled(enabled);
		addButton.setEnabled(enabled);
		deleteButton.setEnabled(enabled);
	}

	private static JLabel createLabel(String text)
	{
		JLabel l = new JLabel(text);
		l.setOpaque(true);
		return l;
	}

	private JButton createButton(String name, String icon, String tooltip)
	{
		JButton button = new JButton();
		button.addActionListener(this);
		button.setToolTipText(tooltip);
		boolean hasIcon = false;
		try
		{
			if (icon != null)
			{
				Image img = ImageIO.read(ClassLoader.getSystemResource(icon));
				ImageIcon icn = new ImageIcon(img);
				button.setIcon(icn);
				button.setText("");
				button.setPreferredSize(new Dimension(icn.getIconWidth() + 8, icn.getIconHeight() + 4));
				hasIcon = true;
			}
		}
		catch (IOException e)
		{
		}

		if (!hasIcon)
			button.setText(name);

		return button;
	}

	private void showAddButton(boolean b)
	{
		Color cb = new Color(150, 75, 200, (b ? 0 : 75));
		Color cf = new Color(0, 0, 0, (b ? 150 : 255));

		nameLabel.setBackground(cb);
		descriptionLabel.setBackground(cb);

		nameLabel.setForeground(cf);
		descriptionLabel.setForeground(cf);

		nameLabel.setEnabled(!b);
		descriptionLabel.setEnabled(!b);
		addButton.setVisible(b);
		deleteButton.setVisible(!b);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		System.out.println("action triggered by " + o.toString());

		showAddButton(false);
	}
}