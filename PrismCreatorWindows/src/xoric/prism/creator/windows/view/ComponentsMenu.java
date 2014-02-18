package xoric.prism.creator.windows.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.creator.windows.control.ISceneControl;

public class ComponentsMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private ISceneControl control;

	private final AddComponentMenu addMenu;
	private final JMenuItem deleteItem;

	public ComponentsMenu()
	{
		super("Components");

		add(addMenu = new AddComponentMenu());

		deleteItem = new JMenuItem("Remove");
		deleteItem.addActionListener(this);
		add(deleteItem);
	}

	public void setControl(ISceneControl control)
	{
		this.control = control;

		addMenu.setControl(control);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == deleteItem)
		{
			control.requestDeleteComponent();
		}
	}
}
