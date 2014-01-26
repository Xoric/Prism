package xoric.prism.creator.models.image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import xoric.prism.swing.tooltips.ToolTipFormatter;

public class SpriteMenu extends JPopupMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final ISpriteMenuListener listener;

	private final JMenuItem cloneItem;
	private final JMenuItem insertItem;
	private final JMenuItem insertFromClipboardItem;
	private final JMenuItem editItem;
	private final JMenuItem deleteItem;
	private final JMenuItem copyImageItem;
	private final JMenuItem copyNameItem;
	private final JMenuItem makeTransparentItem;
	private final JMenuItem reloadItem;

	public SpriteMenu(ISpriteMenuListener listener)
	{
		this.listener = listener;

		add(cloneItem = createItem("Clone"));
		add(insertItem = createItem("Insert new"));
		add(insertFromClipboardItem = createItem("Insert from clipboard"));
		add(new JSeparator());
		add(editItem = createItem("Edit"));
		add(deleteItem = createItem("Delete"));
		add(copyImageItem = createItem("Copy image"));
		add(copyNameItem = createItem("Copy filename"));
		add(makeTransparentItem = createItem("Make transparent"));
		add(new JSeparator());
		add(reloadItem = createItem("Reload all"));

		cloneItem.setToolTipText(ToolTipFormatter.split("Clone the selected sprite and insert it as successor."));
		insertItem.setToolTipText(ToolTipFormatter.split("Insert a new sprite at the selected position."));
		insertFromClipboardItem
				.setToolTipText(ToolTipFormatter.split("Insert a new sprite at the selected position from clipboard."));
		editItem.setToolTipText(ToolTipFormatter.split("Edit the selected sprite. A program can be specified in the menu above."));
		deleteItem.setToolTipText(ToolTipFormatter.split("Delete the selected sprites."));
		copyImageItem.setToolTipText(ToolTipFormatter.split("Copy the image to clipboard."));
		copyNameItem.setToolTipText(ToolTipFormatter.split("Copy the sprite's filename to clipboard."));
		makeTransparentItem.setToolTipText(ToolTipFormatter.split("Cut off the background."));
		reloadItem.setToolTipText(ToolTipFormatter.split("Reload all sprites to make external changes visible."));
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

		if (o == cloneItem)
			listener.requestCloneSprite();
		else if (o == insertItem)
			listener.requestInsertSprite();
		else if (o == insertFromClipboardItem)
			listener.requestInsertSpriteFromClipboard();
		else if (o == editItem)
			listener.requestEditSprite();
		else if (o == deleteItem)
			listener.requestDeleteSprites();
		else if (o == copyImageItem)
			listener.requestCopySpriteImage();
		else if (o == copyNameItem)
			listener.requestCopySpriteFilename();
		else if (o == makeTransparentItem)
			listener.requestMakeTransparent();
		else if (o == reloadItem)
			listener.requestReloadSprites();
	}
}
