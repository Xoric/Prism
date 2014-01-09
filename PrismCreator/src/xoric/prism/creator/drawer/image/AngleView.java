package xoric.prism.creator.drawer.image;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AngleView extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final JLabel frameLabel;
	private final JButton leftButton;
	private final JButton rightButton;
	private final JButton addButton;

	public AngleView()
	{
		super(new GridBagLayout());

		Dimension d = new Dimension(0, 30);
		this.setMaximumSize(d);

		leftButton = createButton("<", "Jump to previous frame");
		rightButton = createButton(">", "Jump to next frame");
		addButton = createButton("+", "Add a frame");
		frameLabel = new JLabel("Frame 0 of 0");

		int x = 0;
		Insets noInsets = new Insets(0, 0, 0, 0);

		GridBagConstraints c = new GridBagConstraints(x++, 0, 1, 1, 0.2, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, noInsets,
				0, 0);
		add(leftButton, c);

		c = new GridBagConstraints(x++, 0, 1, 1, 0.4, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0,
				0);
		add(frameLabel, c);

		c = new GridBagConstraints(x++, 0, 1, 1, 0.2, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, noInsets, 0, 0);
		add(rightButton, c);

		c = new GridBagConstraints(x++, 0, 1, 1, 0.2, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, noInsets, 0, 0);
		add(addButton, c);
	}

	private JButton createButton(String s, String tooltip)
	{
		JButton b = new JButton(s);
		b.setToolTipText(tooltip);
		b.addActionListener(this);
		return b;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		System.out.println("click on " + o.toString());
	}
}
