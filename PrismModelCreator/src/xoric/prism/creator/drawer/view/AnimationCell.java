package xoric.prism.creator.drawer.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.world.entities.AnimationIndex;

public class AnimationCell extends JPanel implements IAnimationCell
{
	private static final long serialVersionUID = 1L;

	protected IDrawerControl control;

	protected AnimationIndex animationIndex;

	private final JLabel nameLabel;
	private final JLabel descriptionLabel;

	public AnimationCell(AnimationIndex a)
	{
		super(new GridBagLayout());

		animationIndex = a;

		nameLabel = createLabel(animationIndex.toString());
		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));

		descriptionLabel = createLabel(animationIndex.getDescription().toLowerCase());

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		add(nameLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
				0);
		add(descriptionLabel, c);
	}

	@Override
	public void displayAnimation(AnimationIndex a)
	{
		// set basic animation info
		animationIndex = a;

		// display basic animation info
		nameLabel.setText(a.toString());
		descriptionLabel.setText(animationIndex.getDescription().toLowerCase());
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
	}

	public AnimationIndex getAnimationIndex()
	{
		return animationIndex;
	}

	private static JLabel createLabel(String text)
	{
		JLabel l = new JLabel(text);
		l.setOpaque(true);
		return l;
	}
}