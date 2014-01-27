package xoric.prism.creator.models.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import xoric.prism.creator.models.control.IMainControl;
import xoric.prism.creator.models.model.VariationList;
import xoric.prism.swing.PrismPanel;
import xoric.prism.swing.PrismSwing;
import xoric.prism.world.animations.AnimationIndex;

public class AnimationList extends PrismPanel implements IAnimationList
{
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
			AnimationIndex a = animations[i];
			int p = a.getPriority();
			if (p > 0)
			{
				AnimationCellWithControls c = new AnimationCellWithControls(animations[i], e);
				insertSorted(c);
			}
		}

		JScrollPane scroll = PrismSwing.createScrollPane(pane);
		//		JScrollPane scroll = new JScrollPane(pane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		//				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//
		//		scroll.getVerticalScrollBar().setUnitIncrement(16);
		//		scroll.getHorizontalScrollBar().setUnitIncrement(16);

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
		boolean isFirst = true;

		pane.removeAll();

		// add used animations
		for (int i = 0; i < list.size(); ++i)
		{
			AnimationCellWithControls c = list.get(i);
			if (c.isUsed())
			{
				if (isFirst)
				{
					isFirst = false;
					pane.add(createPriorityHeader("Currently added", Color.blue), createConstraints(y++));
				}

				if (y > 0)
					pane.add(new JSeparator(), createConstraints(y++));

				pane.add(c, createConstraints(y++));
			}
		}

		// add unused animations
		int p = 0;
		boolean isFirstInPriority;
		do
		{
			++p;
			isFirstInPriority = true;

			for (int i = 0; i < list.size(); ++i)
			{
				AnimationCellWithControls c = list.get(i);
				int priority = c.getAnimationIndex().getPriority();

				if (!c.isUsed() && priority == p)
				{
					if (isFirstInPriority)
					{
						isFirstInPriority = false;
						pane.add(createPriorityHeader(p, Color.gray), createConstraints(y++));
					}
					pane.add(c, createConstraints(y++));
				}
			}
		}
		while (p < 5);

		pane.revalidate();
		pane.repaint();
	}

	private static JComponent createPriorityHeader(int p, Color color)
	{
		String s;
		if (p == 1)
			s = "Primary animations";
		else if (p == 2)
			s = "Secondary animations";
		else if (p == 3)
			s = "Rarely needed";
		else
			s = "Priority level" + p;

		return createPriorityHeader(s, color);
	}

	private static JComponent createPriorityHeader(String s, Color color)
	{
		JLabel l = new JLabel(s);
		Font f = l.getFont().deriveFont(Font.ITALIC);
		l.setFont(f);

		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3,
				6, 3, 3), 0, 0);
		p.add(l, c);

		p.setBorder(BorderFactory.createEtchedBorder());

		return p;
	}

	private static GridBagConstraints createConstraints(int line)
	{
		return new GridBagConstraints(0, line, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0);
	}

	public void setControl(IMainControl control)
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
		for (AnimationCell l : list)
			l.setEnabled(enabled);
	}

	@Override
	public void displayAnimationInList(VariationList list)
	{
		int i = getListIndex(list.getAnimationIndex());
		AnimationCellWithControls c = this.list.get(i);
		c.displayAnimation(list);
		sortList();
	}

	@Override
	public void displayAnimationsInList(VariationList[] list)
	{
		if (list == null)
		{
			for (AnimationIndex a : AnimationIndex.values())
			{
				int i = getListIndex(a);
				if (i >= 0)
				{
					AnimationCellWithControls c = this.list.get(i);
					c.displayAnimationIndex(null);
				}
			}
		}
		else
		{
			for (VariationList l : list)
			{
				int i = getListIndex(l.getAnimationIndex());
				if (i >= 0)
				{
					AnimationCellWithControls c = this.list.get(i);
					c.displayAnimation(l);
				}
			}
		}
		sortList();
	}
}
