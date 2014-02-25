package xoric.prism.creator.ground.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.creator.ground.control.IActionControl;

/**
 * @author XoricLee
 * @since 24.02.2014, 11:17:17
 */
public class ActionMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private IActionControl control;

	private JMenuItem addItem;

	public ActionMenu()
	{
		super("Actions");

		add(addItem = createItem("Add ground"));
	}

	public void setControl(IActionControl control)
	{
		this.control = control;
	}

	private JMenuItem createItem(String text)
	{
		JMenuItem m = new JMenuItem(text);
		m.addActionListener(this);
		return m;
	}

	// ActionListener:
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == addItem)
			control.requestAddGround();
	}
}
