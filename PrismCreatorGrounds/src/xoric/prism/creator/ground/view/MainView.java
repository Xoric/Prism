package xoric.prism.creator.ground.view;

import javax.swing.JMenuBar;

import xoric.prism.creator.ground.control.IMainControl;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.swing.PrismFrame;

/**
 * @author XoricLee
 * @since 22.02.2014, 20:17:24
 */
public class MainView extends PrismFrame implements IMainView
{
	private static final long serialVersionUID = 1L;

	private IMainControl control;

	private final SceneHandler sceneHandler;
	private final ActionMenu actionMenu;

	private final GroundPanel groundPanel;

	public MainView(SceneHandler sceneHandler) throws PrismException
	{
		super("Ground Creator", 640, 400, true);

		this.sceneHandler = sceneHandler;
		this.actionMenu = new ActionMenu();
		this.groundPanel = new GroundPanel();

		JMenuBar mb = new JMenuBar();
		mb.add(actionMenu);
		this.setJMenuBar(mb);

		this.setContentPane(groundPanel);

		selectGround(-1);
	}

	public void setControl(IMainControl control)
	{
		this.control = control;
		actionMenu.setControl(control);
		sceneHandler.setControl(control);
		groundPanel.setControl(control);
	}

	//	public void start() throws PrismException
	//	{
	//		AllGrounds.loadAll(Prism.global.loadMetaFile(FileTableDirectoryIndex.WORLD, WorldIndex.GROUND.ordinal()));
	//		AllDrawableGrounds.loadAll();
	//		sceneHandler.start();
	//	}

	@Override
	public void selectGround(int index) throws PrismException
	{
		sceneHandler.setSelectedGround(index);
		groundPanel.displayGround(index);
	}
}
