package xoric.prism.creator.drawer.view;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.image.AnimationView;
import xoric.prism.creator.drawer.image.IAnimationView;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.settings.WorkingDirs;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.scene.IScene;
import xoric.prism.swing.PrismFrame;
import xoric.prism.world.animations.AnimationIndex;

public class DrawerView extends PrismFrame implements IDrawerView, IAnimationEditor
{
	private static final long serialVersionUID = 1L;
	private static final String title = "Prism Drawer";

	private IScene scene;
	private DrawerModel model;
	private IDrawerControl control;

	private ModelTable modelTable;
	private PortraitPanel portraitPanel;
	private AnimationList animationList;
	private IAnimationView animationView;
	private JPanel animationViewPanel;
	private final MainMenuBar mainMenuBar;

	public DrawerView(IScene scene)
	{
		super(title, 640, 480, true);

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		this.scene = scene;

		// main menu
		mainMenuBar = new MainMenuBar();
		setJMenuBar(mainMenuBar);

		// model table
		ModelTable m = new ModelTable();
		modelTable = m;

		// portrait panel
		portraitPanel = new PortraitPanel();

		// animation list
		animationList = new AnimationList(this);
		animationList.setEnabled(false);

		// animation view
		AnimationView v = new AnimationView(this);
		animationViewPanel = v;
		animationView = v;

		Insets insets = new Insets(15, 15, 15, 15);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 2, 0.15, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0);
		add(m, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.85, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(portraitPanel, c);

		c = new GridBagConstraints(1, 1, 1, 1, 0.85, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animationList, c);

		// initially hidden:
		c = new GridBagConstraints(0, 0, 2, 2, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animationViewPanel, c);

		showAnimationControls(false);
	}

	/* *********** IDrawerView ********************** */

	@Override
	public void setModel(DrawerModel model)
	{
		this.model = model;
		mainMenuBar.setModel(model);
	}

	@Override
	public void displayAll(DrawerModel model)
	{
		displayName(model == null ? null : model.getName());
		displaySpriteSize(model == null ? null : model.getSpriteSize());
		displayPortrait(model == null ? null : model.getPath());
		displayPath(model == null ? null : model.getPath());
		displayAnimationsInList(model == null ? null : model.getAnimations());

		showAnimationControls(false);
	}

	@Override
	public void displayName(IText_r name)
	{
		modelTable.displayName(name);
	}

	@Override
	public void displaySpriteSize(IPoint_r tileSize)
	{
		modelTable.displaySpriteSize(tileSize);
		animationView.setTileSize(tileSize);
	}

	@Override
	public void displayPortrait(IPath_r path)
	{
		portraitPanel.displayPortrait(path);
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
	}

	@Override
	public void reloadCurrentAnimationFrames()
	{
		animationView.reloadCurrentAnimationFrames();
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

	/* *********** IAnimationPanel ********************** */

	@Override
	public void displayCurrentAnimationDuration()
	{
		animationView.displayCurrentAnimationDuration();
	}

	/* ********** internal *********************** */

	private void showAnimationControls(boolean showAnimationView)
	{
		modelTable.setVisible(!showAnimationView);
		portraitPanel.setVisible(!showAnimationView);
		animationList.setVisible(!showAnimationView);
		animationViewPanel.setVisible(showAnimationView);
	}

	public void setControl(IDrawerControl control)
	{
		this.control = control;

		// pass control to sub classes
		this.modelTable.setControl(control);
		this.portraitPanel.setControl(control);
		this.animationList.setControl(control);
		this.animationView.setControl(control);
		this.mainMenuBar.setControl(control);
	}

	public void start()
	{
		setVisible(true);

		control.initialize();
	}

	@Override
	public void displayRecentDirectories(WorkingDirs dirs)
	{
		mainMenuBar.displayRecentDirectories(dirs);
	}
}
