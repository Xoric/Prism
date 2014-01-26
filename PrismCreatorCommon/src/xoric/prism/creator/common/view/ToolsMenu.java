package xoric.prism.creator.common.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import xoric.prism.creator.common.tools.ExternalImageEditor;

public class ToolsMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JMenuItem menuItemEditor;

	public ToolsMenu()
	{
		super("Tools");

		this.menuItemEditor = createMenuItem("Choose external image editor");
	}

	private JMenuItem createMenuItem(String text)
	{
		JMenuItem m = new JMenuItem(text);
		m.addActionListener(this);
		add(m);
		return m;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == menuItemEditor)
			ExternalImageEditor.getInstance().showInput();
	}
}
