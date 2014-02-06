package xoric.prism.creator.common.view;

import java.awt.Component;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.data.tools.Common;

public class MainMenuBar extends JMenuBar implements IMainMenuBar
{
	private static final long serialVersionUID = 1L;

	private final MainMenu mainMenu;
	private final ToolsMenu toolsMenu;
	private final HelpMenu helpMenu;

	public MainMenuBar(String dataName, INewDialogCreator d, boolean useToolsMenu)
	{
		String s = Common.GAME_NAME + " " + dataName.substring(0, 1).toUpperCase() + dataName.substring(1) + "Creator";

		add(mainMenu = new MainMenu(dataName, d));
		if (useToolsMenu)
			add(toolsMenu = new ToolsMenu());
		else
			toolsMenu = null;
		add(helpMenu = new HelpMenu(s));
	}

	@Override
	public void setMainMenuListener(IMainMenuListener mainMenuListener)
	{
		mainMenu.setMainMenuListener(mainMenuListener);
	}

	@Override
	public void setDialogCreator(INewDialogCreator dialogCreator)
	{
		mainMenu.setDialogCreator(dialogCreator);
	}

	@Override
	public void appendAboutHtmlLine(String line)
	{
		helpMenu.getAboutDialog().appendHtmlLine(line);
	}

	@Override
	public void addCreationItem(JMenuItem item)
	{
		mainMenu.addCreationItem(item);
	}

	@Override
	public void setModelObjectIsNull(boolean isModelObjectNull)
	{
		mainMenu.setModelObjectIsNull(isModelObjectNull);
	}

	@Override
	public void appendAboutComponent(Component c)
	{
		helpMenu.getAboutDialog().appendComponent(c);
	}
}
