package xoric.prism.creator.drawer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import xoric.prism.scene.IScene;
import xoric.prism.swing.PrismFrame;

public class PrismDrawer extends PrismFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private IScene scene;
	private DrawerModel model;

	private IModelTable modelTable;

	private JMenu menuModel;
	private JMenuItem menuItemNewModel;
	private JMenuItem menuItemOpenModel;
	private JMenuItem menuItemSaveModel;
	private JMenuItem menuItemExit;

	private JMenu menuAnimation;
	private JMenu menuNewAnimation;
	private JMenu menuSelectAnimation;
	private JMenu menuRemoveAnimation;

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

		ModelTable m = new ModelTable();
		modelTable = m;
		add(BorderLayout.CENTER, m);
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

	private boolean saveModelAs()
	{
		return false;
	}

	private boolean askSaveChanges()
	{
		boolean isOK = true;

		if (model != null && model.hasChanges())
		{
			int result = JOptionPane.showConfirmDialog(null,
					"The current model contains unsaved changes. Would you like to save before closing?", "Save changes",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == 0) // 0: Yes, save changes
				isOK = saveModelAs();
			else
				isOK = result == 1; // 1: No, discard | 2: Cancel
		}
		return isOK;
	}

	private void onNewModel()
	{
		boolean isOK = askSaveChanges();
		if (isOK)
		{
			model = new DrawerModel();
			modelTable.setModel(model);
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
