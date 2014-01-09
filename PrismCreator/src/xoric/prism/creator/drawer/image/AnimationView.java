package xoric.prism.creator.drawer.image;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import xoric.prism.swing.PrismFrame;

public class AnimationView extends JPanel
{
	private static final long serialVersionUID = 1L;

	private AngleSelectorPanel angleSelectorPanel;
	private AngleView angleView;

	public AnimationView()
	{
		super(new GridBagLayout());

		angleSelectorPanel = new AngleSelectorPanel(false);
		angleView = new AngleView();

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
				0, 0, 0, 0), 0, 0);
		add(angleSelectorPanel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		add(angleView, c);
	}

	public static void main(String[] args)
	{
		AnimationView p = new AnimationView();

		JFrame f = new PrismFrame("Test", 300, 200, true);
		f.setContentPane(p);

		f.setVisible(true);

		AngleSelectorPanel d = new AngleSelectorPanel(true);
		d.showDialog();
	}
}
