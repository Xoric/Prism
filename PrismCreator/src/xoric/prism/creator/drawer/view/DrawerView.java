package xoric.prism.creator.drawer.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xoric.prism.creator.drawer.IModelTableListener;
import xoric.prism.creator.drawer.control.DrawerControl;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.scene.IScene;
import xoric.prism.swing.PrismFrame;

public class DrawerView extends PrismFrame implements ActionListener, IModelTableListener
{
	private static final long serialVersionUID = 1L;
	private static final String title = "Prism Drawer";

	private IScene scene;
	private DrawerModel model;

	private ModelTable modelTable;
	private AnimationList animationList;
	//	private ModelPaint modelPaint;

	private JMenu menuModel;
	private JMenuItem menuItemNewModel;
	private JMenuItem menuItemOpenModel;
	private JMenuItem menuItemSaveModel;
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

		modelPaint = new ModelPaint(scene);

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

		ModelTable m = new ModelTable(this);
		modelTable = m;
		animationList = new AnimationList();

		Insets insets = new Insets(15, 15, 15, 15);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.15, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				insets, 0, 0);
		add(m, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.85, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(animationList, c);

		setModel(null);
		enableAnimationList();
		enableSaveButton();
	}

	private void setModel(DrawerModel model)
	{
		if (model != null)
			this.setTitle(title + " - " + model.getPath().toString());
		else
			this.setTitle(title);

		this.model = model;
		modelTable.setModel(model);
	}

	private void enableAnimationList()
	{
		if (animationList != null)
			animationList.setEnabled(model != null && model.getTileSize().getX() > 0 && model.getTileSize().getY() > 0
					&& model.getPath().exists());
	}

	private void enableSaveButton()
	{
		boolean canSave = model != null && model.hasChanges();
		menuItemSaveModel.setEnabled(canSave);
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

	private void onNewModel()
	{
		if (DrawerControl.askSaveChanges(model))
		{
			DrawerModel newModel = DrawerControl.showNewModelDialog();

			if (newModel != null)
			{
				setModel(newModel);
				enableAnimationList();
				enableSaveButton();
			}
		}
	}

	private void onOpenModel()
	{
		if (DrawerControl.askSaveChanges(model))
		{
			DrawerModel newModel = DrawerControl.showOpenModelDialog();

			if (newModel != null)
			{
				setModel(newModel);
				enableAnimationList();
				enableSaveButton();
			}
		}
	}

	private void onSaveModel()
	{
		DrawerControl.saveChanges(model);
		enableSaveButton();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == menuItemNewModel)
			onNewModel();
		else if (o == menuItemOpenModel)
			onOpenModel();
		else if (o == menuItemSaveModel)
			onSaveModel();
	}

	@Override
	public void notifyModelTableValueChanged()
	{
		enableAnimationList();
		enableSaveButton();
	}
}
