package xoric.prism.creator.drawer.image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class SpriteMenu extends JPopupMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final ISpriteMenuListener listener;

	private final JMenuItem insertItem;
	private final JMenuItem deleteItem;

	public SpriteMenu(ISpriteMenuListener listener)
	{
		this.listener = listener;

		add(insertItem = createItem("Insert new frame"));
		add(new JSeparator());
		add(deleteItem = createItem("Delete"));
	}

	private JMenuItem createItem(String s)
	{
		JMenuItem item = new JMenuItem(s);
		item.addActionListener(this);
		return item;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == insertItem)
			listener.requestInsertSprite();
	}
}
