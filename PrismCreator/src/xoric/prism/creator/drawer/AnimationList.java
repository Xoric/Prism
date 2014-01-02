package xoric.prism.creator.drawer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;

import xoric.prism.world.entities.Animation;

public class AnimationList extends JPanel
{
	private static final long serialVersionUID = 1L;

	public AnimationList()
	{
		super(new BorderLayout());
		JPanel pane = new JPanel(new GridBagLayout());

		int y = 0;

		Animation[] animations = Animation.values();

		for (int i = 0; i < animations.length; ++i)
		{
			if (i > 0)
			{
				JSeparator s = new JSeparator();
				GridBagConstraints c = new GridBagConstraints(0, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0);
				pane.add(s, c);
			}

			Animation a = animations[i];

			AnimationLine l = new AnimationLine(a);
			GridBagConstraints c = new GridBagConstraints(0, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0);
			pane.add(l, c);
		}

		JScrollPane scroll = new JScrollPane(pane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(BorderLayout.CENTER, scroll);
	}
}
