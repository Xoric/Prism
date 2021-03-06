package xoric.prism.creator.common.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class HelpMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final JMenuItem menuItemAbout;
	private final AboutDialog aboutDialog;

	public HelpMenu(String creatorName)
	{
		super("Help");

		menuItemAbout = new JMenuItem("About");
		menuItemAbout.addActionListener(this);

		add(menuItemAbout);

		aboutDialog = new AboutDialog(creatorName);
	}

	public AboutDialog getAboutDialog()
	{
		return aboutDialog;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == menuItemAbout)
			aboutDialog.showAbout();
	}
}
