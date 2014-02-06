package xoric.prism.creator.windows.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import xoric.prism.creator.common.view.INewDialog;
import xoric.prism.creator.common.view.PrismCreatorCommonView;
import xoric.prism.creator.windows.control.IMainControl;
import xoric.prism.creator.windows.control.SceneHandler;
import xoric.prism.creator.windows.model.WindowModel;
import xoric.prism.creator.windows.view.tree.WindowTree;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.scene.IScene;

public class MainView extends PrismCreatorCommonView implements IMainView
{
	private static final long serialVersionUID = 1L;

	private final IPoint_r resolution;
	private SceneHandler sceneHandler;
	private final FloatRect screenRect;
	private final IScene scene;
	private WindowModel model;
	private IMainControl control;

	private final WindowTree tree;

	public MainView(IScene scene)
	{
		super("window", false);

		resolution = scene.findBestResolution(640, 480);
		screenRect = new FloatRect(0.0f, 0.0f, resolution.getX(), resolution.getY());

		JPanel p = new JPanel(new GridBagLayout());
		this.setContentPane(p);

		this.scene = scene;
		//		this.sceneHandler = new SceneHandler(scene, resolution);

		tree = new WindowTree();

		GridBagConstraints c = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				30, 30, 30, 30), 0, 0);
		p.add(tree, c);
	}

	public void setControl(IMainControl control)
	{
		super.getMainMenuBar().setMainMenuListener(control);
		this.control = control;

		if (sceneHandler != null)
			sceneHandler.setControl(control);

		tree.setControl(control);
	}

	@Override
	public INewDialog createDialog()
	{
		return new NewWindowDialog();
	}

	@Override
	public void setModel(WindowModel model)
	{
		this.model = model;

		tree.setModel(model);

		if (sceneHandler != null)
			sceneHandler.setModel(model);
	}

	@Override
	public void startScene()
	{
		if (model == null)
		{
			closeScene();
		}
		else
		{
			if (sceneHandler != null)
			{
				sceneHandler.requestStop();
			}
			sceneHandler = new SceneHandler(scene, resolution);
			sceneHandler.setModel(model);
			sceneHandler.setControl(control);
			sceneHandler.start();
		}
	}

	@Override
	public void closeScene()
	{
		sceneHandler.requestStop();
		sceneHandler = null;
	}

	@Override
	public void displayTree()
	{
		tree.display();
	}

	@Override
	public IFloatPoint_r getScreenSize()
	{
		return screenRect.getSize();
	}

	@Override
	public IFloatRect_r getScreenRect()
	{
		return screenRect;
	}
}
