package xoric.prism.creator.ground.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xoric.prism.creator.ground.control.IGroundControl;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.swing.input.fields.PrismIntField;
import xoric.prism.world.map.AllGrounds;
import xoric.prism.world.map2.GroundType2;

/**
 * @author XoricLee
 * @since 24.02.2014, 17:23:11
 */
public class GroundPanel extends JPanel implements ChangeListener, ActionListener
{
	private static final long serialVersionUID = 1L;

	private IGroundControl control;

	private GroundType2 ground;

	private final PrismIntField startField;
	private final PrismIntField countField;
	private final PrismIntField intervalField;

	private final JLabel countLabel;
	private final JLabel intervalLabel;

	private final JCheckBox animatedCheckbox;

	private final JButton resetButton;
	private final JButton okButton;

	public GroundPanel()
	{
		super(new BorderLayout());
		JPanel p = new JPanel(new GridBagLayout());

		Insets insets = new Insets(5, 30, 5, 30);
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
				0);

		startField = new PrismIntField(0);
		countField = new PrismIntField(1);
		intervalField = new PrismIntField(400);

		animatedCheckbox = new JCheckBox("animated");
		animatedCheckbox.addChangeListener(this);

		resetButton = createButton("Reset");
		okButton = createButton("OK");

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(resetButton);
		buttonPanel.add(okButton);

		c.gridx = 0;
		p.add(new JLabel("animationStart"), c);
		c.gridx = 1;
		p.add(startField, c);

		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		p.add(animatedCheckbox, c);
		c.gridwidth = 1;

		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		p.add(new JSeparator(), c);
		c.gridwidth = 1;

		c.gridy++;
		c.gridx = 0;
		p.add(countLabel = new JLabel("animationCount"), c);
		c.gridx = 1;
		p.add(countField, c);

		c.gridy++;
		c.gridx = 0;
		p.add(intervalLabel = new JLabel("animationInterval"), c);
		c.gridx = 1;
		p.add(intervalField, c);

		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		p.add(new JSeparator(), c);
		c.gridwidth = 1;

		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		p.add(buttonPanel, c);
		c.gridwidth = 1;

		add(p, BorderLayout.CENTER);
		setAnimationEnabled(false);
	}

	private JButton createButton(String text)
	{
		JButton b = new JButton(text);
		b.addActionListener(this);
		return b;
	}

	public void displayGround(int index) throws PrismException
	{
		ground = index < 0 ? null : AllGrounds.getGroundType(index);
		displayGround();
	}

	private void displayGround()
	{
		startField.setText(ground == null ? "" : String.valueOf(ground.getAnimationStart()));
		int n = ground == null ? 0 : ground.getAnimationCount();
		countField.setText(ground == null ? "" : String.valueOf(n));
		intervalField.setText(ground == null ? "" : String.valueOf(ground.getAnimationInterval()));
		animatedCheckbox.setSelected(ground != null && n > 1);

		startField.setEnabled(ground != null);
		animatedCheckbox.setEnabled(ground != null);
		resetButton.setEnabled(ground != null);
		okButton.setEnabled(ground != null);
	}

	// ChangeListener:
	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object o = e.getSource();

		if (o == animatedCheckbox)
		{
			boolean b = animatedCheckbox.isSelected();
			setAnimationEnabled(b);
		}
	}

	private void setAnimationEnabled(boolean b)
	{
		countField.setEnabled(b);
		intervalField.setEnabled(b);
		countLabel.setEnabled(b);
		intervalLabel.setEnabled(b);
	}

	public void setControl(IGroundControl control)
	{
		this.control = control;
	}

	private void applyChanges()
	{
		int animationStart = startField.getInt();
		boolean isAnimated = animatedCheckbox.isSelected();

		int animationCount = isAnimated ? countField.getInt() : 0;
		int animationInterval = isAnimated ? intervalField.getInt() : 0;

		ground.initialize(animationStart, animationCount, animationInterval);
	}

	// ActionListener:
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == resetButton)
		{
			displayGround();
		}
		else if (o == okButton)
		{
			applyChanges();
			control.onGroundModified();
		}
	}
}
