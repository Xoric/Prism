package xoric.prism.creator.common.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;

public class RecentMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final WorkingDirs workingDirs;
	private IMainMenuListener control;

	public RecentMenu(String project)
	{
		super("Recent");
		workingDirs = new WorkingDirs(project);
		workingDirs.load();
		displayDirectories();
	}

	public void setControl(IMainMenuListener control)
	{
		this.control = control;
	}

	public void addPath(IPath_r path)
	{
		workingDirs.addWorkingDirectory(path);
		displayDirectories();
	}

	private void displayDirectories()
	{
		this.removeAll();

		List<IPath_r> dirs = workingDirs.getDirectories();
		int n = dirs.size();
		if (n > 6)
			n = 6;

		for (int i = 0; i < n; ++i)
		{
			IPath_r p = dirs.get(i);
			String s = p.toString();
			JMenuItem m = new JMenuItem(s);
			add(m);
			m.setActionCommand(s);
			m.addActionListener(this);
		}
		this.setEnabled(n > 0);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String s = e.getActionCommand();

		if (s != null && s.length() > 0)
			control.requestOpenProject(new Path(s));
	}

	public void setMainMenuListener(IMainMenuListener mainMenuListener)
	{
		this.control = mainMenuListener;
	}
}
