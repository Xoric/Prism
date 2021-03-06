package xoric.prism.creator.models.view;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import xoric.prism.creator.common.view.INewDialog;
import xoric.prism.creator.common.view.INewDialogCreator;
import xoric.prism.creator.common.view.PrismCreatorCommonView;
import xoric.prism.creator.models.control.IMainControl;
import xoric.prism.creator.models.image.AnimationView;
import xoric.prism.creator.models.image.IAnimationView;
import xoric.prism.creator.models.model.ModelModel;
import xoric.prism.creator.models.model.VariationList;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.world.animations.AnimationIndex;

public class MainView extends PrismCreatorCommonView implements IMainView, IAnimationEditor, INewDialogCreator
{
	private static final long serialVersionUID = 1L;

	private ModelModel model;
	//	private IMainControl control;

	private ModelTable modelTable;
	private PortraitPanel portraitPanel;
	private AnimationList animationList;
	private IAnimationView animationView;
	private JPanel animationViewPanel;
	private final ModelMenuBar mainMenuBar;

	public MainView()
	{
		super("model", true);
		super.getMainMenuBar().setDialogCreator(this);

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		// main menu
		mainMenuBar = new ModelMenuBar(this);
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

	/* *********** IMainView ********************** */

	@Override
	public void setModel(ModelModel model)
	{
		this.model = model;
		this.mainMenuBar.setModel(model);
	}

	@Override
	public void displayAll(ModelModel model)
	{
		displayName(model == null ? null : model.getName());
		displaySpriteSize(model == null ? null : model.getSpriteSize());
		displayPortrait(model == null ? null : model.getPath());
		displayPath(model == null ? null : model.getPath());
		displayAnimationsInList(model == null ? null : model.getAnimationList());

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
		super.setExtendedTitle(path == null ? null : path.toString());
	}

	@Override
	public void displayAnimationsInList(VariationList[] list)
	{
		animationList.setEnabled(list != null);
		animationList.displayAnimationsInList(list);
	}

	@Override
	public void displayAnimationInList(VariationList list)
	{
		animationList.displayAnimationInList(list);
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

		VariationList l = model.getAnimation(a);
		animationView.displayAnimation(l);
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

	public void setControl(IMainControl control)
	{
		//		this.control = control;

		// pass control to sub classes
		this.modelTable.setControl(control);
		this.portraitPanel.setControl(control);
		this.animationList.setControl(control);
		this.animationView.setControl(control);
		this.mainMenuBar.setControl(control);
		super.getMainMenuBar().setMainMenuListener(control);
	}

	public void start()
	{
		setVisible(true);
	}

	@Override
	public INewDialog createDialog()
	{
		return new NewModelDialog();
	}
}
