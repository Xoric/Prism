package xoric.prism.creator.drawer.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;

import xoric.prism.creator.drawer.control.IDrawerControl2;
import xoric.prism.swing.PrismPanel;
import xoric.prism.world.entities.AnimationIndex;

public class AnimationList extends PrismPanel
{
	private static final long serialVersionUID = 1L;

	private List<AnimationLine> list;

	public AnimationList()
	{
		super("Animations");

		JPanel pane = new JPanel(new GridBagLayout());

		int y = 0;

		list = new ArrayList<AnimationLine>();
		AnimationIndex[] animations = AnimationIndex.values();

		for (int i = 0; i < animations.length; ++i)
		{
			if (i > 0)
			{
				JSeparator s = new JSeparator();
				GridBagConstraints c = new GridBagConstraints(0, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0);
				pane.add(s, c);
			}

			AnimationIndex a = animations[i];

			AnimationLine l = new AnimationLine(a);
			GridBagConstraints c = new GridBagConstraints(0, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0);
			pane.add(l, c);
			list.add(l);
		}

		JScrollPane scroll = new JScrollPane(pane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		setContent(scroll);
	}

	public void setControl(IDrawerControl2 control)
	{
		for (AnimationLine l : list)
			l.setControl(control);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		for (AnimationLine l : list)
			l.setEnabled(enabled);
	}
}
