package xoric.prism.creator.common.view;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xoric.prism.creator.common.control.IMainMenuListener;

public class MainMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;

	private final MainMenu mainMenu;
	private final ToolsMenu toolsMenu;

	public MainMenuBar(String dataName)
	{
		add(mainMenu = new MainMenu(dataName));
		add(toolsMenu = new ToolsMenu());
	}

	public void setMainMenuListener(IMainMenuListener mainMenuListener)
	{
		mainMenu.setMainMenuListener(mainMenuListener);
	}

	protected void setDialogCreator(INewDialogCreator dialogCreator)
	{
		mainMenu.setDialogCreator(dialogCreator);
	}

	protected void addCreationItem(JMenuItem item)
	{
		mainMenu.addCreationItem(item);
	}

	public void setModelObjectIsNull(boolean isModelObjectNull)
	{
		mainMenu.setModelObjectIsNull(isModelObjectNull);
	}
}
