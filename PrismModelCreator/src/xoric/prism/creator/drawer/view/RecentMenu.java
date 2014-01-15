package xoric.prism.creator.drawer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.settings.WorkingDirs;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;

public class RecentMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private IDrawerControl control;

	public RecentMenu()
	{
		super("Recent");
	}

	public void setControl(IDrawerControl control)
	{
		this.control = control;
	}

	public void displayDirectories(WorkingDirs workingDirs)
	{
		this.removeAll();

		List<IPath_r> dirs = workingDirs.getDirectories();
		int n = dirs.size();
		if (n > 5)
			n = 5;

		for (int i = 0; i < n; ++i)
		{
			IPath_r p = dirs.get(i);
			String s = p.toString();
			JMenuItem m = new JMenuItem(s);
			add(m);
			m.setActionCommand(s);
			m.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String s = e.getActionCommand();

		if (s != null && s.length() > 0)
			control.requestOpenRecent(new Path(s));
	}
}
