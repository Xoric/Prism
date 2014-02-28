package xoric.prism.creator.common.spritelist.view;

import java.awt.Component;
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
	private final JMenuItem hotSpotItem;
	private final JMenuItem editItem;
	private final JMenuItem deleteItem;
	private final JMenuItem copyImageItem;
	private final JMenuItem copyNameItem;
	private final JMenuItem makeTransparentItem;
	private final JMenuItem reloadItem;

	private JSeparator hotSpotSeparator;

	public SpriteMenu(ISpriteMenuListener listener)
	{
		this.listener = listener;

		add(cloneItem = createItem("Clone"));
		add(insertItem = createItem("Insert new"));
		add(insertFromClipboardItem = createItem("Insert from clipboard"));
		add(new JSeparator());
		add(hotSpotItem = createItem("Edit hotspot and action points"));
		add(hotSpotSeparator = new JSeparator());
		add(editItem = createItem("Edit"));
		add(deleteItem = createItem("Delete"));
		add(copyImageItem = createItem("Copy image"));
		add(copyNameItem = createItem("Copy filename"));
		add(makeTransparentItem = createItem("Make transparent"));
		add(new JSeparator());
		add(reloadItem = createItem("Reload all"));

		cloneItem.setToolTipText(ToolTipFormatter.split("Clone the selected sprite and insert it as successor."));
		insertItem.setToolTipText(ToolTipFormatter.split("Insert a new sprite at the selected position."));
		insertFromClipboardItem.setToolTipText(ToolTipFormatter.split("Insert a new sprite at the selected position from clipboard."));
		hotSpotItem.setToolTipText(ToolTipFormatter
				.split("Specify how to position this sprite and supply action points where to place external objects etc."));
		editItem.setToolTipText(ToolTipFormatter.split("Edit the selected sprite. A program can be specified in the menu above."));
		deleteItem.setToolTipText(ToolTipFormatter.split("Delete the selected sprites."));
		copyImageItem.setToolTipText(ToolTipFormatter.split("Copy the image to clipboard."));
		copyNameItem.setToolTipText(ToolTipFormatter.split("Copy the sprite's filename to clipboard."));
		makeTransparentItem.setToolTipText(ToolTipFormatter.split("Cut off the background."));
		reloadItem.setToolTipText(ToolTipFormatter.split("Reload all sprites to make external changes visible."));

		registerHotSpotListener(null);
	}

	public void registerHotSpotListener(IHotSpotListener hotSpotListener)
	{
		boolean b = hotSpotListener != null;

		hotSpotItem.setVisible(b);
		hotSpotSeparator.setVisible(b);
	}

	private JMenuItem createItem(String s)
	{
		JMenuItem item = new JMenuItem(s);
		item.addActionListener(this);
		return item;
	}

	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		for (Component c : this.getComponents())
			c.setEnabled(b);
	}

	public void requestHotSpot()
	{
		listener.requestSetSpriteHotspot();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == cloneItem)
			listener.requestCloneSprite();
		else if (o == insertItem)
			listener.requestInsertNewSprite();
		else if (o == insertFromClipboardItem)
			listener.requestInsertSpriteFromClipboard();
		else if (o == hotSpotItem)
			requestHotSpot();
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
