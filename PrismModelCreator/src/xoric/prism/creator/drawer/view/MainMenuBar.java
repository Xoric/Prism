package xoric.prism.creator.drawer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import xoric.prism.creator.drawer.control.IDrawerControl;

public class MainMenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final String modelExtension = ".md";

	private IDrawerControl control;

	private JMenu menuModel;
	private JMenuItem menuItemNewModel;
	private JMenuItem menuItemOpenModel;
	private JMenuItem menuItemCloseModel;
	private JMenu menuExportModel;
	private JMenuItem menuItemExit;

	private JMenuItem menuItemGenerate;
	private JMenuItem menuItemExport;

	private JMenu menuTools;
	private JMenuItem menuItemExternalEditor;

	public MainMenuBar()
	{
		// model menu
		menuModel = createMenu(this, "Model");
		// --
		menuItemNewModel = createMenuItem(menuModel, "New");
		menuItemOpenModel = createMenuItem(menuModel, "Open");
		menuModel.addSeparator(); // --
		menuExportModel = createMenu(menuModel, "Export");
		menuItemCloseModel = createMenuItem(menuModel, "Close");
		menuModel.addSeparator(); // --
		menuItemExit = createMenuItem(menuModel, "Exit");
		// --

		menuItemGenerate = createMenuItem(menuExportModel, "Generate animations");
		menuItemExport = createMenuItem(menuExportModel, "Export model (" + modelExtension + ")");

		// tools menu
		menuTools = createMenu(this, "Tools");
		// --
		menuItemExternalEditor = createMenuItem(menuTools, "External image editor");
		// --
	}

	public void setControl(IDrawerControl control)
	{
		this.control = control;
	}

	private JMenuItem createMenuItem(JMenu parentMenu, String text)
	{
		JMenuItem m = new JMenuItem(text);
		m.addActionListener(this);
		parentMenu.add(m);

		return m;
	}

	private JMenu createMenu(JComponent parent, String text)
	{
		JMenu m = new JMenu(text);
		parent.add(m);

		return m;
	}

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

		else if (o == menuItemGenerate)
			control.requestGenerateAnimations();
		else if (o == menuItemExport)
			control.requestExportModel();

		else if (o == menuItemExternalEditor)
			control.requestInputExternalImageEditor();
	}
}