package xoric.prism.creator.common.view;

import xoric.prism.data.tools.Common;
import xoric.prism.swing.PrismFrame;

public class PrismCreatorCommonView extends PrismFrame implements INewDialogCreator
{
	private static final long serialVersionUID = 1L;

	private final String dataName;

	private final MainMenuBar mainMenuBar;
	private final String baseTitle;

	public PrismCreatorCommonView(String dataName)
	{
		super("", 640, 480, true);
		this.dataName = dataName;

		baseTitle = Common.GAME_NAME + " " + dataName.substring(0, 1).toUpperCase() + dataName.substring(1) + "Creator";
		setExtendedTitle(null);

		mainMenuBar = new MainMenuBar(dataName, this);
		this.setJMenuBar(mainMenuBar);
	}

	protected IMainMenuBar getMainMenuBar()
	{
		return mainMenuBar;
	}

	//	protected void setMainMenuListener(IMainMenuListener mainMenuListener)
	//	{
	//		mainMenuBar.setMainMenuListener(mainMenuListener);
	//	}

	//	protected void setDialogCreator(INewDialogCreator dialogCreator)
	//	{
	//		mainMenuBar.setDialogCreator(dialogCreator);
	//	}

	public void setExtendedTitle(String s)
	{
		this.setTitle(baseTitle + (s == null ? "" : " - " + s));
	}

	@Override
	public INewDialog createDialog()
	{
		return new DefaultNewDialog(dataName);
	}
}
