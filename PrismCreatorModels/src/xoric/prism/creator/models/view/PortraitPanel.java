package xoric.prism.creator.models.view;

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

import xoric.prism.creator.common.factory.IconLoader;
import xoric.prism.creator.models.control.IMainControl;
import xoric.prism.data.types.IPath_r;
import xoric.prism.swing.PrismPanel;
import xoric.prism.swing.tooltips.ToolTipFormatter;

public class PortraitPanel extends PrismPanel implements ActionListener, IPortraitView
{
	private static final long serialVersionUID = 1L;

	private IMainControl control;

	private IPath_r path;

	private final JLabel iconLabel;
	private final JButton newButton;
	private final JButton importButton;
	private final JButton editButton;
	private final JButton deleteButton;
	private final JButton reloadButton;

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
		buttonPanel.add(importButton = createButton("...", null, "Import an existing image as portrait."), c);
		c.gridx++;
		buttonPanel.add(editButton = createButton("Edit", "icons/edit.png", "Edit this portrait."), c);
		c.gridx++;
		buttonPanel.add(deleteButton = createButton("Delete", "icons/del.png", "Delete this portrait."), c);
		c.gridx++;
		buttonPanel.add(reloadButton = createButton("Reload", "icons/refresh.png", "Reload portrait."), c);

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		p.add(iconLabel = new JLabel(), c);
		iconLabel.setToolTipText(ToolTipFormatter.split("Supply a portrait image for the model."));

		c = new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 30, 0, 0), 0, 0);
		p.add(buttonPanel, c);

		displayPortrait(null);
	}

	public void setControl(IMainControl control)
	{
		this.control = control;
	}

	@Override
	public void displayPortrait(IPath_r path)
	{
		this.path = path;

		reloadPortrait();

		boolean b = path != null;
		newButton.setEnabled(b);
		importButton.setEnabled(b);
		editButton.setEnabled(b);
		deleteButton.setEnabled(b);
		reloadButton.setEnabled(b);
	}

	private void reloadPortrait()
	{
		boolean hasPortrait = loadPortrait();

		newButton.setVisible(!hasPortrait);
		importButton.setVisible(!hasPortrait);

		editButton.setVisible(hasPortrait);
		deleteButton.setVisible(hasPortrait);

		reloadButton.setVisible(path != null);
	}

	private boolean loadPortrait()
	{
		boolean hasPortrait = false;
		ImageIcon icon = null;
		File portraitFile = path == null ? null : path.getFile("portrait.png");
		String note = "N/A";

		if (portraitFile != null && portraitFile.exists())
		{
			try
			{
				icon = IconLoader.loadIconFromFile(portraitFile, 100, 100);
				hasPortrait = true;
			}
			catch (Exception e)
			{
				note = "corrupt file";
			}
		}
		else
		{
			try
			{
				icon = IconLoader.loadIconFromResource("icons/port.png");
			}
			catch (Exception e)
			{
			}
		}

		// refresh icon
		icon.getImage().flush();
		iconLabel.setText(icon == null ? note : "");
		iconLabel.setIcon(icon);
		iconLabel.revalidate();

		return hasPortrait;
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

		b.setToolTipText(ToolTipFormatter.split(tooltip));
		b.addActionListener(this);

		return b;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == newButton)
			control.requestCreateNewPortrait();
		else if (o == importButton)
			control.requestImportPortrait();
		else if (o == editButton)
			control.requestEditPortrait();
		else if (o == deleteButton)
			control.requestDeletePortrait();
		else if (o == reloadButton)
			reloadPortrait();
	}
}
