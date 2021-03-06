package xoric.prism.creator.common.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;
import xoric.prism.swing.input.OpenPathDialog;

public class MainMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final String dataName;
	private IMainMenuListener listener;
	private INewDialogCreator dialogCreator;

	private JMenuItem menuItemNew;
	private JMenuItem menuItemOpen;
	private RecentMenu recentMenu;
	private JMenu menuCreate;
	private JMenuItem menuItemClose;
	private JMenuItem menuItemExit;

	public MainMenu(String dataName, INewDialogCreator d)
	{
		super(dataName.substring(0, 1).toUpperCase() + dataName.substring(1));
		this.dataName = dataName;
		this.dialogCreator = d;

		menuItemNew = createMenuItem("New");
		addSeparator();

		menuItemOpen = createMenuItem("Open");
		recentMenu = new RecentMenu(dataName);
		add(recentMenu);
		addSeparator();

		menuCreate = new JMenu("Create");
		add(menuCreate);
		menuItemClose = createMenuItem("Close");
		addSeparator();

		menuItemExit = createMenuItem("Exit");
	}

	public void addCreationItem(JMenuItem item)
	{
		menuCreate.add(item);
	}

	private JMenuItem createMenuItem(String text)
	{
		JMenuItem m = new JMenuItem(text);
		m.addActionListener(this);
		add(m);
		return m;
	}

	public void onNewObject()
	{
		INewDialog d = dialogCreator.createDialog();
		boolean b = d.showDialog();
		if (b)
		{
			INewDialogResult result = d.getResult();
			IPath_r path = result.getPath();
			recentMenu.addPath(path);
			listener.requestCreateNewProject(result);
		}
	}

	public void onOpenObject()
	{
		OpenPathDialog d = new OpenPathDialog("Open " + dataName, "Please enter the source working directory to load from.");
		boolean b = d.showOpenPathDialog();
		Path path = b ? d.getResult() : null;

		if (path != null)
		{
			recentMenu.addPath(path);
			listener.requestOpenProject(path);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == menuItemNew)
			onNewObject();
		else if (o == menuItemOpen)
			onOpenObject();
		else if (o == menuItemClose)
			listener.requestCloseProject();
		else if (o == menuItemExit)
			listener.requestExit();
	}

	public void setMainMenuListener(IMainMenuListener mainMenuListener)
	{
		this.listener = mainMenuListener;
		this.recentMenu.setMainMenuListener(mainMenuListener);
	}

	protected void setDialogCreator(INewDialogCreator dialogCreator)
	{
		this.dialogCreator = dialogCreator;
	}

	public void setModelObjectIsNull(boolean isModelObjectNull)
	{
		menuCreate.setEnabled(!isModelObjectNull);
		menuItemClose.setEnabled(!isModelObjectNull);
	}
}
