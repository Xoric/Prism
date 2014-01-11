package xoric.prism.creator.drawer.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xoric.prism.creator.drawer.control.IDrawerControl2;
import xoric.prism.creator.drawer.image.AnimationView;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.scene.IScene;
import xoric.prism.swing.PrismFrame;

public class DrawerView2 extends PrismFrame implements IDrawerView2, ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final String title = "Prism Drawer";

	private IScene scene;
	private DrawerModel model;
	private IDrawerControl2 control;

	private ModelTable modelTable;
	private AnimationList animationList;
	private AnimationView animationView;

	private JMenu menuModel;
	private JMenuItem menuItemNewModel;
	private JMenuItem menuItemOpenModel;
	private JMenuItem menuItemSaveModel;
	private JMenuItem menuItemExit;

	private JMenu menuAnimation;
	private JMenu menuNewAnimation;
	private JMenu menuSelectAnimation;
	private JMenu menuRemoveAnimation;

	public DrawerView2(IScene scene)
	{
		super(title, 640, 480, true);

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		this.scene = scene;

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// model menu
		menuModel = createMenu(menuBar, "Model");
		// --
		menuItemNewModel = createMenuItem(menuModel, "New");
		menuModel.addSeparator(); // --
		menuItemOpenModel = createMenuItem(menuModel, "Open");
		menuItemSaveModel = createMenuItem(menuModel, "Save");
		menuModel.addSeparator(); // --
		menuItemExit = createMenuItem(menuModel, "Exit");
		// --

		// animation menu
		menuAnimation = createMenu(menuBar, "Animation");
		// --
		menuNewAnimation = createMenu(menuAnimation, "New");
		menuSelectAnimation = createMenu(menuAnimation, "Select");
		menuRemoveAnimation = createMenu(menuAnimation, "Remove");
		// --

		// model table
		ModelTable m = new ModelTable();
		modelTable = m;

		// animation list
		animationList = new AnimationList();

		// animation view
		animationView = new AnimationView();

		Insets insets = new Insets(15, 15, 15, 15);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.15, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0);
		add(m, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.85, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animationList, c);

		c = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animationView, c);

		showControls(false);
	}

	/* *********** IDrawerView ********************** */

	@Override
	public void displayAll(DrawerModel model)
	{
		displayName(model.getName());
		displayTileSize(model.getTileSize());
		displayPath(model.getPath());
		displaySaveState(model.hasChanges());
		displayAnimations(model.getAnimations());
	}

	@Override
	public void displayName(IText_r name)
	{
		modelTable.displayName(name);
	}

	@Override
	public void displayTileSize(IPoint_r tileSize)
	{
		modelTable.displayTileSize(tileSize);
	}

	@Override
	public void displayPath(IPath_r path)
	{
		if (path != null)
			this.setTitle(title + " - " + path.toString());
		else
			this.setTitle(title);
	}

	@Override
	public void displaySaveState(boolean canSave)
	{
		menuItemSaveModel.setEnabled(canSave);
	}

	@Override
	public void displayAnimations(AnimationModel[] animations)
	{
		for (AnimationModel m : animations)
			animationList.displayAnimation(m);
	}

	@Override
	public void displayAnimation(AnimationModel m)
	{
		animationList.displayAnimation(m);
	}

	@Override
	public void displayAnimationImages(AnimationModel m)
	{
		animationView.displayAnimationImages(m);
		showControls(true);
	}

	/* ********** internal *********************** */

	private void showControls(boolean showAnimationView)
	{
		modelTable.setVisible(!showAnimationView);
		animationList.setVisible(!showAnimationView);
		animationView.setVisible(showAnimationView);
	}

	public void setControl(IDrawerControl2 control)
	{
		this.control = control;

		// pass control to sub classes
		this.modelTable.setControl(control);
		this.animationList.setControl(control);
	}

	private JMenuItem createMenuItem(JMenu parentMenu, String text)
	{
		JMenuItem m = new JMenuItem(text);

		if (parentMenu != null)
			parentMenu.add(m);

		m.addActionListener(this);

		return m;
	}

	private JMenu createMenu(JMenu parentMenu, String text)
	{
		JMenu m = new JMenu(text);

		if (parentMenu != null)
			parentMenu.add(m);

		return m;
	}

	private JMenu createMenu(JMenuBar parentMenuBar, String text)
	{
		JMenu m = new JMenu(text);

		if (parentMenuBar != null)
			parentMenuBar.add(m);

		return m;
	}

	public void start()
	{
		setVisible(true);
	}

	/* *********** menu ********************** */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == menuItemNewModel)
			//			control.requestNewModel();
			onNewModel();
		else if (o == menuItemOpenModel)
			control.requestOpenModel();
		else if (o == menuItemSaveModel)
			control.requestSaveModel();
	}

	private void onNewModel()
	{
		boolean isOK = control.askSaveChanges();
		if (isOK)
		{
			NewModelDialog d = new NewModelDialog();
			isOK = d.show();

			if (isOK)
				control.requestNewModel(d.getResult());
		}
	}
}
