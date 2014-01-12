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
	private JMenuItem menuItemCloseModel;
	private JMenuItem menuItemExit;

	private JMenu menuTools;
	private JMenuItem menuItemExternalEditor;

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
		menuModel.addSeparator(); // --
		menuItemOpenModel = createMenuItem(menuModel, "Open");
		menuItemCloseModel = createMenuItem(menuModel, "Close");
		menuModel.addSeparator(); // --
		menuItemExit = createMenuItem(menuModel, "Exit");
		// --

		// tools menu
		menuTools = createMenu(menuBar, "Tools");
		// --
		menuItemExternalEditor = createMenuItem(menuTools, "External image editor");
		// --

		// model table
		ModelTable m = new ModelTable();
		modelTable = m;

		// animation list
		animationList = new AnimationList(this);
		animationList.setEnabled(false);

		// animation view
		AnimationView v = new AnimationView(this);
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
		displayAnimationsInList(model == null ? null : model.getAnimations());
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
		animationView.setTileSize(tileSize);
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
	public void displayAnimationsInList(AnimationModel[] ms)
	{
		animationList.setEnabled(ms != null);
		animationList.displayAnimationsInList(ms);
	}

	@Override
	public void displayAnimationInList(AnimationModel m)
	{
		animationList.displayAnimationInList(m);
		//		animationView.displayAnimation(m);
	}

	@Override
	public void updateCurrentAnimation()
	{
		animationView.updateCurrentAnimation();
	}

	@Override
	public void setHourglass(boolean b)
	{
		if (b)
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		else
			setCursor(Cursor.getDefaultCursor());
	}

	/* *********** IAnimationEditor ********************** */

	@Override
	public void requestEditAnimation(AnimationIndex a)
	{
		setHourglass(true);

		AnimationModel m = model.getAnimation(a);
		animationView.displayAnimation(m);
		showAnimationControls(true);

		setHourglass(false);
	}

	@Override
	public void requestCloseAnimationEditor()
	{
		showAnimationControls(false);
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
		else if (o == menuItemCloseModel)
			control.requestCloseModel();

		else if (o == menuItemExternalEditor)
			control.requestInputExternalImageEditor();
	}
}
