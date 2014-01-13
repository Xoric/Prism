package xoric.prism.creator.drawer.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.creator.drawer.image.IconLoader;
import xoric.prism.data.types.IPath_r;
import xoric.prism.swing.PrismPanel;

public class PortraitPanel extends PrismPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final JLabel iconLabel;
	private final JButton newButton;
	private final JButton editButton;
	private final JButton importButton;

	public PortraitPanel()
	{
		super("Portrait");

		JPanel p = new JPanel(new GridBagLayout());
		setContent(p);

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0), 0, 0);
		buttonPanel.add(newButton = createButton("New", "icons/add.png", "Create a new portrait image."), c);
		c.gridx++;
		buttonPanel.add(editButton = createButton("Edit", "icons/edit.png", "Edit the portrait image."), c);
		c.gridx++;
		buttonPanel.add(importButton = createButton("...", null, "Import an existing image as portrait."), c);

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		p.add(iconLabel = new JLabel(), c);

		c = new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 30, 0, 0), 0, 0);
		p.add(buttonPanel, c);

		updatePortrait(null);
	}

	private void enableButtons()
	{

	}

	private void updatePortrait(IPath_r path)
	{
		ImageIcon icon = null;
		File portraitFile = path == null ? null : path.getFile("portrait.png");
		try
		{
			if (portraitFile != null && portraitFile.exists())
				icon = IconLoader.loadIconFromFile(portraitFile, 100, 100);
			else
				icon = IconLoader.loadIconFromResource("icons/port.png");
		}
		catch (Exception e)
		{
			icon = null;
		}

		if (icon != null)
		{
			iconLabel.setIcon(icon);
		}
		else
		{
			iconLabel.setText("N/A");
			iconLabel.setIcon(null);
		}
	}

	private JButton createButton(String text, String iconResource, String tooltip)
	{
		JButton b = new JButton();
		boolean hasIcon = false;
		try
		{
			if (iconResource != null)
			{
				ImageIcon icon = IconLoader.loadIconFromResource(iconResource);
				b.setIcon(icon);
				hasIcon = true;
			}
		}
		catch (Exception e)
		{
			hasIcon = false;
		}
		if (!hasIcon)
			b.setText(text);

		b.setToolTipText(tooltip);
		b.addActionListener(this);

		return b;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

	}
}
