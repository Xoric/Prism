package xoric.prism.creator.drawer.view;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.image.AnimationView;
import xoric.prism.creator.drawer.image.IAnimationView;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.scene.IScene;
import xoric.prism.swing.PrismFrame;
import xoric.prism.world.entities.AnimationIndex;

public class DrawerView extends PrismFrame implements IDrawerView2, ActionListener, IAnimationEditor
{
	private static final long serialVersionUID = 1L;
	private static final String title = "Prism Drawer";

	private IScene scene;
	private DrawerModel model;
	private IDrawerControl control;

	private ModelTable modelTable;
	private AnimationList animationList;
	private IAnimationView animationView;
	private JPanel animationViewPanel;

	private JMenu menuModel;
	private JMenuItem menuItemNewModel;
	private JMenuItem menuItemOpenModel;
	private JMenuItem menuItemSaveModel;
	private JMenuItem menuItemCloseModel;
	private JMenuItem menuItemExit;

	private JMenu menuAnimation;
	private JMenu menuNewAnimation;
	private JMenu menuSelectAnimation;
	private JMenu menuRemoveAnimation;

	public DrawerView(IScene scene)
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
		menuItemOpenModel = createMenuItem(menuModel, "Open");
		menuModel.addSeparator(); // --
		menuItemSaveModel = createMenuItem(menuModel, "Save");
		menuItemSaveModel.setEnabled(false);
		menuItemCloseModel = createMenuItem(menuModel, "Close");
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
		animationList = new AnimationList(this);

		// animation view
		AnimationView v = new AnimationView();
		animationViewPanel = v;
		animationView = v;

		Insets insets = new Insets(15, 15, 15, 15);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.15, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0);
		add(m, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.85, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animationList, c);

		c = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animationViewPanel, c);

		showAnimationControls(false);
	}

	/* *********** IDrawerView ********************** */

	@Override
	public void setModel(DrawerModel model)
	{
		this.model = model;
	}

	@Override
	public void displayAll(DrawerModel model)
	{
		displayName(model == null ? null : model.getName());
		displayTileSize(model == null ? null : model.getTileSize());
		displayPath(model == null ? null : model.getPath());
		displayAnimations(model == null ? null : model.getAnimations());
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
	public void displayAnimations(AnimationModel[] animations)
	{
		if (animations == null)
		{
			animationList.setEnabled(false);
			animationList.displayEmpty();
		}
		else
		{
			animationList.setEnabled(true);
			for (AnimationModel m : animations)
				animationList.displayAnimation(m);
		}
	}

	@Override
	public void displayAnimation(AnimationModel m)
	{
		animationList.displayAnimation(m);
		animationView.displayAnimationImages(m);
	}

	@Override
	public void setHourglass(boolean b)
	{
		if (b)
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		else
			setCursor(Cursor.getDefaultCursor());
		// TODO test
	}

	/* *********** IAnimationEditor ********************** */

	@Override
	public void requestEditAnimation(AnimationIndex a)
	{
		setHourglass(true);

		AnimationModel m = model.getAnimation(a);
		animationView.displayAnimationImages(m);
		showAnimationControls(true);

		setHourglass(false);
	}

	/* ********** internal *********************** */

	private void showAnimationControls(boolean showAnimationView)
	{
		modelTable.setVisible(!showAnimationView);
		animationList.setVisible(!showAnimationView);
		animationViewPanel.setVisible(showAnimationView);
	}

	public void setControl(IDrawerControl control)
	{
		this.control = control;

		// pass control to sub classes
		this.modelTable.setControl(control);
		this.animationList.setControl(control);
		this.animationView.setControl(control);
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
			control.requestNewModel();
		else if (o == menuItemOpenModel)
			control.requestOpenModel();
		//		else if (o == menuItemSaveModel)
		//			control.requestSaveModel();
		else if (o == menuItemCloseModel)
			control.requestCloseModel();
	}
}
