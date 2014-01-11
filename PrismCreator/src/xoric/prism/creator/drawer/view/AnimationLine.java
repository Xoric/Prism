package xoric.prism.creator.drawer.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.world.entities.AnimationIndex;

public class AnimationLine extends JPanel implements IAnimationLine
{
	private static final long serialVersionUID = 1L;

	protected AnimationIndex animation;

	protected boolean isUsed;

	private final JLabel nameLabel;
	private final JLabel infoLabel;
	private final JLabel descriptionLabel;
	//	private final JButton addButton;
	//	private final JButton editButton;
	//	private final JButton deleteButton;

	protected IDrawerControl control;

	public AnimationLine(AnimationIndex animation)
	{
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		this.animation = animation;
		this.isUsed = false;

		nameLabel = createLabel(animation.toString());
		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));

		infoLabel = new JLabel();
		descriptionLabel = createLabel(animation.getDescription().toLowerCase());
		//		addButton = createButton("Add", "icons/add.png", "Click here to add this animation");
		//		editButton = createButton("Edit", "icons/edit.png", "Click here to edit this animation");
		//		deleteButton = createButton("Delete", "icons/del.png", "Click here to delete this animation");

		//		JPanel buttonPanel = new JPanel(new FlowLayout());
		//		buttonPanel.add(addButton);
		//		buttonPanel.add(editButton);
		//		buttonPanel.add(deleteButton);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		add(nameLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
				0);
		add(descriptionLabel, c);

		//		c = new GridBagConstraints(1, 0, 1, 2, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		//		add(buttonPanel, c);
		//
		//		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		//		showAddButton(true);
	}

	public boolean isUsed()
	{
		return isUsed;
	}

	public void setControl(IDrawerControl control)
	{
		this.control = control;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		nameLabel.setEnabled(enabled);
		descriptionLabel.setEnabled(enabled);
		//		addButton.setEnabled(enabled);
		//		deleteButton.setEnabled(enabled);
	}

	private static JLabel createLabel(String text)
	{
		JLabel l = new JLabel(text);
		l.setOpaque(true);
		return l;
	}

	public AnimationIndex getAnimationIndex()
	{
		return animation;
	}

	public String getAnimationName()
	{
		return nameLabel.getText();
	}

	@Override
	public void displayAnimation(AnimationModel m)
	{
		// show/hide buttons
		isUsed = m != null && m.isUsed();
		//		showAddButton(!isUsed);

		// display other information
		// TODO
	}
}
