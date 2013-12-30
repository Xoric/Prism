package xoric.prism.creator.drawer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xoric.prism.scene.IScene;
import xoric.prism.swing.PrismFrame;

public class PrismDrawer extends PrismFrame
{
	private static final long serialVersionUID = 1L;

	private IScene scene;
	private DrawerModel model;

	public PrismDrawer(IScene scene)
	{
		super("Prism Drawer", 640, 480, true);

		this.scene = scene;
		this.model = new DrawerModel();

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnModel = new JMenu("Model");
		menuBar.add(mnModel);

		JMenuItem mntmNewModel = new JMenuItem("New");
		mnModel.add(mntmNewModel);

		JMenuItem mntmLoadModel = new JMenuItem("Open");
		mnModel.add(mntmLoadModel);

		JMenuItem mntmSaveModel = new JMenuItem("Save as");
		mnModel.add(mntmSaveModel);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnModel.add(mntmExit);

		JMenu mnAnimation = new JMenu("Animation");
		menuBar.add(mnAnimation);

		JMenu mnAdd = new JMenu("Add");
		mnAnimation.add(mnAdd);

		JMenu mnSelect = new JMenu("Select");
		mnAnimation.add(mnSelect);

		JMenu mnRemove = new JMenu("Remove");
		mnAnimation.add(mnRemove);
	}

	public void start()
	{
		setVisible(true);
	}
}
