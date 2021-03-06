package xoric.prism.creator.models.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import xoric.prism.creator.models.model.VariationList;
import xoric.prism.swing.tooltips.ToolTipFormatter;
import xoric.prism.world.animations.AnimationIndex;

public class AnimationCellWithControls extends AnimationCell implements ActionListener, IAnimationCellControls
{
	private static final long serialVersionUID = 1L;

	private final IAnimationEditor mainView;

	private boolean isUsed;

	private final JButton addButton;
	private final JButton editButton;
	private final JButton deleteButton;

	public AnimationCellWithControls(AnimationIndex a, IAnimationEditor e)
	{
		super(a);

		mainView = e;
		isUsed = false;

		addButton = createButton("Add", "icons/add.png", "Click here to add this animation.");
		editButton = createButton("Edit", "icons/edit.png", "Click here to edit this animation.");
		deleteButton = createButton("Delete", "icons/del.png", "Click here to delete this animation.");

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		buttonPanel.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints(1, 0, 1, 2, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
				0, 0, 0), 0, 0);
		add(buttonPanel, c);

		setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
		showAddButton(true);

		float f = 0.85f;
		float g = f * getBackground().getRGB() + (1.0f - f) * Color.blue.getRGB();
		this.setBackground(new Color((int) g));
		updateColor();
	}

	public boolean isUsed()
	{
		return isUsed;
	}

	private void updateColor()
	{
		setOpaque(isUsed);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		addButton.setEnabled(enabled);
		deleteButton.setEnabled(enabled);
	}

	private void showAddButton(boolean b)
	{
		addButton.setVisible(b);
		editButton.setVisible(!b);
		deleteButton.setVisible(!b);
	}

	@Override
	public void displayAnimation(VariationList list)
	{
		if (list != null)
			super.displayAnimationIndex(list.getAnimationIndex());

		isUsed = list == null ? false : list.isUsed();

		updateColor();

		// show/hide buttons
		showAddButton(!isUsed);

		// TODO display additional information
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == addButton)
			control.requestAddAnimation(animationIndex);
		else if (o == editButton)
			mainView.requestEditAnimation(animationIndex);
		else if (o == deleteButton)
			control.requestDeleteAnimation(animationIndex, -1);
	}

	private JButton createButton(String s, String icon, String tooltip)
	{
		JButton b = new JButton();
		b.addActionListener(this);
		boolean hasIcon = false;
		try
		{
			if (icon != null && icon.length() > 0)
			{
				Image img = ImageIO.read(ClassLoader.getSystemResource(icon));
				ImageIcon icn = new ImageIcon(img);
				b.setIcon(icn);
				Dimension d = new Dimension(icn.getIconWidth() + 8, icn.getIconHeight() + 8);
				b.setPreferredSize(d);
				b.setMinimumSize(d);
				b.setMaximumSize(d);
				b.setToolTipText(ToolTipFormatter.split(tooltip));
				hasIcon = true;
			}
		}
		catch (Exception e)
		{
		}
		if (!hasIcon)
			b.setText(s);

		return b;
	}
}
