package xoric.prism.creator.drawer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.world.entities.Animation;

public class AnimationLine extends JPanel implements ActionListener, IAnimationLine
{
	private static final long serialVersionUID = 1L;

	private final JLabel nameLabel;
	private final JLabel directionsLabel;
	private final JButton directionsButton;
	private final JLabel descriptionLabel;
	private final JButton addButton;
	private final JButton switchButton;
	private final JButton deleteButton;

	public AnimationLine(Animation animation, int width)
	{
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEtchedBorder());

		Dimension d = new Dimension(width, 35);
		setMaximumSize(d);
		setPreferredSize(d);

		nameLabel = new JLabel("<html><b>" + animation.toString() + "</b></html>");
		directionsLabel = new JLabel();
		directionsButton = new JButton();
		descriptionLabel = new JLabel(animation.getDescription().toLowerCase());
		addButton = new JButton();
		switchButton = new JButton();
		deleteButton = new JButton();

		directionsButton.addActionListener(this);
		addButton.addActionListener(this);
		switchButton.addActionListener(this);
		deleteButton.addActionListener(this);

		this.add(BorderLayout.CENTER, nameLabel);
		this.add(BorderLayout.SOUTH, descriptionLabel);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		System.out.println("action triggered by " + o.toString());
	}
}
