package xoric.prism.swing;

import java.awt.Component;

import javax.swing.JScrollPane;

public abstract class PrismSwing
{
	public static JScrollPane createScrollPane(Component c)
	{
		JScrollPane s = new JScrollPane(c);
		s.getVerticalScrollBar().setUnitIncrement(16);
		s.getHorizontalScrollBar().setUnitIncrement(16);
		return s;
	}
}
