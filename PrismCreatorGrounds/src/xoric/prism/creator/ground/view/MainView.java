package xoric.prism.creator.ground.view;

import xoric.prism.creator.common.view.PrismCreatorCommonView;
import xoric.prism.creator.ground.control.IMainControl;
import xoric.prism.creator.ground.model.AllDrawableGrounds;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.global.WorldIndex;
import xoric.prism.world.map.AllGrounds;

/**
 * @author XoricLee
 * @since 22.02.2014, 20:17:24
 */
public class MainView extends PrismCreatorCommonView implements IMainView
{
	private static final long serialVersionUID = 1L;

	private IMainControl control;

	private final SceneHandler sceneHandler;
	private final ActionMenu actionMenu;

	private final GroundPanel groundPanel;

	public MainView(SceneHandler sceneHandler) throws PrismException
	{
		super("ground", false);
		this.sceneHandler = sceneHandler;
		this.actionMenu = new ActionMenu();
		this.groundPanel = new GroundPanel();

		super.getMainMenuBar().addMenu(actionMenu);

		this.setContentPane(groundPanel);

		selectGround(-1);
	}

	public void setControl(IMainControl control)
	{
		this.control = control;
		this.getMainMenuBar().setMainMenuListener(control);
		actionMenu.setControl(control);
		sceneHandler.setControl(control);
		groundPanel.setControl(control);
	}

	public void start() throws PrismException
	{
		AllGrounds.loadAll(Prism.global.loadMetaFile(FileTableDirectoryIndex.WORLD, WorldIndex.GROUND.ordinal()));
		AllDrawableGrounds.loadAll();
		sceneHandler.start();
	}

	// IMainView:
	@Override
	public void selectGround(int index) throws PrismException
	{
		sceneHandler.setSelectedGround(index);
		groundPanel.displayGround(index);
	}
}
