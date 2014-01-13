package xoric.prism.creator.drawer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.swing.PrismPanel;
import xoric.prism.world.animations.AnimationIndex;

public class AnimationList extends PrismPanel implements IAnimationList
{
	// TODO: reimplement as JList

	private static final long serialVersionUID = 1L;

	private List<AnimationCellWithControls> list;
	private final JPanel pane;

	public AnimationList(IAnimationEditor e)
	{
		super("Animations");

		pane = new JPanel(new GridBagLayout());
		list = new ArrayList<AnimationCellWithControls>();
		AnimationIndex[] animations = AnimationIndex.values();

		// create a line for each animation
		for (int i = 0; i < animations.length; ++i)
		{
			AnimationCellWithControls c = new AnimationCellWithControls(animations[i], e);
			insertSorted(c);
		}

		JScrollPane scroll = new JScrollPane(pane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		setContent(scroll);
		sortList();
	}

	private void insertSorted(AnimationCellWithControls c)
	{
		String b = c.getAnimationIndex().toString();

		for (int i = 0; i < list.size(); ++i)
		{
			String a = list.get(i).getAnimationIndex().toString();

			if (b.compareTo(a) < 0)
			{
				list.add(i, c);
				return;
			}
		}
		list.add(c);
	}

	public void sortList()
	{
		int y = 0;

		pane.removeAll();

		// add used animations
		for (int i = 0; i < list.size(); ++i)
		{
			AnimationCellWithControls c = list.get(i);
			if (c.isUsed())
			{
				if (y > 0)
					pane.add(new JSeparator(), createConstraints(y++));

				pane.add(c, createConstraints(y++));
			}
		}

		if (y > 0)
		{
			JPanel p = new JPanel();
			p.setMinimumSize(new Dimension(0, 3));
			p.setOpaque(true);
			p.setBackground(new Color(0, 0, 0, 75));
			pane.add(p, createConstraints(y++));
		}
		int y2 = y;

		// add unused animations
		for (int i = 0; i < list.size(); ++i)
		{
			AnimationCellWithControls c = list.get(i);
			if (!c.isUsed())
			{
				if (y > y2)
					pane.add(new JSeparator(), createConstraints(y++));

				pane.add(c, createConstraints(y++));
			}
		}

		pane.revalidate();
		pane.repaint();
	}

	private static GridBagConstraints createConstraints(int line)
	{
		return new GridBagConstraints(0, line, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0);
	}

	public void setControl(IDrawerControl control)
	{
		for (AnimationCell l : list)
			l.setControl(control);
	}

	private int getListIndex(AnimationIndex a)
	{
		for (int i = 0; i < list.size(); ++i)
			if (list.get(i).getAnimationIndex() == a)
				return i;

		return -1;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		//		super.setEnabled(enabled);

		for (AnimationCell l : list)
			l.setEnabled(enabled);
	}

	@Override
	public void displayAnimationInList(AnimationModel m)
	{
		int i = getListIndex(m.getAnimationIndex());
		AnimationCellWithControls c = list.get(i);
		c.displayAnimationModel(m);
		sortList();
	}

	@Override
	public void displayAnimationsInList(AnimationModel[] ms)
	{
		if (ms == null)
		{
			for (AnimationIndex a : AnimationIndex.values())
			{
				int i = getListIndex(a);
				AnimationCellWithControls c = list.get(i);
				c.displayAnimationModel(null);
			}
		}
		else
		{
			for (AnimationModel m : ms)
			{
				int i = getListIndex(m.getAnimationIndex());
				AnimationCellWithControls l = list.get(i);
				l.displayAnimationModel(m);
			}
		}
		sortList();
	}
}
