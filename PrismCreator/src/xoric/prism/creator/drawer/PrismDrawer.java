package xoric.prism.creator.drawer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xoric.prism.scene.IScene;
import xoric.prism.swing.PrismFrame;

public class PrismDrawer extends PrismFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private IScene scene;
	private DrawerModel model;

	private JMenu menuModel;
	private JMenuItem menuItemNewModel;
	private JMenuItem menuItemOpenModel;
	private JMenuItem menuItemSaveModel;
	private JMenuItem menuItemExit;

	private JMenu menuAnimation;
	private JMenu menuNewAnimation;
	private JMenu menuSelectAnimation;
	private JMenu menuRemoveAnimation;

	private ModelTable modelTable;

	public PrismDrawer(IScene scene)
	{
		super("Prism Drawer", 640, 480, true);

		this.scene = scene;
		this.model = new DrawerModel();

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// model menu
		menuModel = createMenu(menuBar, "Model");
		// --
		menuItemNewModel = createMenuItem(menuModel, "New");
		menuModel.addSeparator(); // --
		menuItemOpenModel = createMenuItem(menuModel, "Open");
		menuItemSaveModel = createMenuItem(menuModel, "Save as");
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

		modelTable = new ModelTable();
		add(BorderLayout.CENTER, modelTable);
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

	private boolean askForChanges()
	{
		// TODO
		//		if (model != null)
		// ...
		return true;
	}

	private void onNewModel()
	{
		boolean isOK = askForChanges();
		if (isOK)
		{
			model = new DrawerModel();

		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == menuItemNewModel)
			onNewModel();
	}
}
