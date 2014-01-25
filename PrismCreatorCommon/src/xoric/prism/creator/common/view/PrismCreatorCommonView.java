package xoric.prism.creator.common.view;

import xoric.prism.creator.common.control.IMainMenuListener;
import xoric.prism.data.tools.Common;
import xoric.prism.swing.PrismFrame;

public class PrismCreatorCommonView extends PrismFrame
{
	private static final long serialVersionUID = 1L;

	private final MainMenuBar mainMenuBar;
	private final String baseTitle;

	public PrismCreatorCommonView(String dataName)
	{
		super("", 640, 480, true);
		baseTitle = Common.GAME_NAME + " " + dataName.substring(0, 1).toUpperCase() + dataName.substring(1) + "Creator";
		setExtendedTitle(null);

		mainMenuBar = new MainMenuBar(dataName);
		this.setJMenuBar(mainMenuBar);
	}

	protected MainMenuBar getMainMenuBar()
	{
		return mainMenuBar;
	}

	protected void setMainMenuListener(IMainMenuListener mainMenuListener)
	{
		mainMenuBar.setMainMenuListener(mainMenuListener);
	}

	protected void setDialogCreator(INewDialogCreator dialogCreator)
	{
		mainMenuBar.setDialogCreator(dialogCreator);
	}

	public void setExtendedTitle(String s)
	{
		this.setTitle(baseTitle + (s == null ? "" : " - " + s));
	}
}
