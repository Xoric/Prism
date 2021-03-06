package xoric.prism.creator.common.factory;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import xoric.prism.swing.tooltips.ToolTipFormatter;

public abstract class Factory
{
	public static JButton createAddButton(ActionListener listener, String tooltip)
	{
		return createButton(listener, "Add", "icons/add.png", tooltip);
	}

	public static JButton createEditButton(ActionListener listener, String tooltip)
	{
		return createButton(listener, "Edit", "icons/edit.png", tooltip);
	}

	public static JButton createDeleteButton(ActionListener listener, String tooltip)
	{
		return createButton(listener, "Delete", "icons/del.png", tooltip);
	}

	public static JButton createUpButton(ActionListener listener, String tooltip)
	{
		return createButton(listener, "Up", "icons/up.png", tooltip);
	}

	public static JButton createDownButton(ActionListener listener, String tooltip)
	{
		return createButton(listener, "Down", "icons/down.png", tooltip);
	}

	public static JButton createButton(ActionListener listener, String s, String icon, String tooltip)
	{
		JButton b = new JButton();
		b.addActionListener(listener);
		boolean hasIcon = false;
		try
		{
			if (icon != null && icon.length() > 0)
			{
				Image img = ImageIO.read(ClassLoader.getSystemResource(icon));
				ImageIcon icn = new ImageIcon(img);
				b.setIcon(icn);
				Dimension d = new Dimension(icn.getIconWidth() + 8, icn.getIconHeight() + 8);
				b.setPreferredSize(d);
				b.setMinimumSize(d);
				b.setMaximumSize(d);
				b.setToolTipText(ToolTipFormatter.split(tooltip));
				hasIcon = true;
			}
		}
		catch (Exception e)
		{
		}
		if (!hasIcon)
			b.setText(s);

		return b;
	}
}
