package xoric.prism.creator.windows.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.client.ui.UIIdentifier;
import xoric.prism.creator.windows.control.ISceneControl;

public class AddComponentMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private ISceneControl control;

	public AddComponentMenu()
	{
		super("Add");

		for (UIIdentifier id : UIIdentifier.values())
			add(createMenuItem(id));
	}

	private JMenuItem createMenuItem(UIIdentifier id)
	{
		JMenuItem m = new JMenuItem(id.toString());
		m.addActionListener(this);
		m.setActionCommand(id.toString());
		return m;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JMenuItem)
		{
			String a = e.getActionCommand();
			UIIdentifier id = UIIdentifier.valueOf(a);
			control.requestAddComponent(id);
		}
	}

	public void setControl(ISceneControl control)
	{
		this.control = control;
	}
}
