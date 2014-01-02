package xoric.prism.creator.drawer;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import xoric.prism.world.entities.Animation;

public class AnimationList extends JPanel
{
	private static final long serialVersionUID = 1L;

	public AnimationList(int width)
	{
		BoxLayout b = new BoxLayout(this, BoxLayout.Y_AXIS);
		Dimension d = new Dimension(width, 100);
		this.setLayout(b);
		this.setPreferredSize(d);
		this.setMaximumSize(d);

		for (Animation a : Animation.values())
		{
			AnimationLine l = new AnimationLine(a, width);
			add(l);
		}
	}
}
