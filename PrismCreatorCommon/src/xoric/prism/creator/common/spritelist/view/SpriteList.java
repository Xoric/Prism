package xoric.prism.creator.common.spritelist.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xoric.prism.creator.common.spritelist.control.SpriteListControl;
import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.common.tools.ExternalImageEditor;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

public class SpriteList extends JPanel implements MouseListener, ISpriteList, ISpriteMenuListener
{
	private static final long serialVersionUID = 1L;

	private SpriteNameGenerator spriteNameGenerator;

	private final HotSpotView hotSpotView;
	private IHotSpotListener hotSpotListener;

	private ImageIcon newIcon;
	private final SpriteMenu menu;
	private final JLabel hintLabel;
	private final JLabel hintLabel2;

	private IPoint_r fixedSpriteSize;
	private IPoint_r tempSpriteSize;

	private final JList<SpriteCell> list;

	public SpriteList()
	{
		super(new GridBagLayout());

		hintLabel = new JLabel("Double click on a sprite in order to edit it.");
		hintLabel2 = new JLabel("Right click on the list below for further options.");

		hotSpotView = new HotSpotView();

		list = new JList<SpriteCell>();
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(1);
		list.setCellRenderer(new SpriteCellRenderer());
		list.addMouseListener(this);

		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		menu = new SpriteMenu(this);
		list.setComponentPopupMenu(menu);

		Insets insets = new Insets(0, 0, 0, 0);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(hintLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(hintLabel2, c);

		c = new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(15, 0, 0, 0), 0, 0);
		add(scroll, c);
	}

	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		list.setEnabled(b);
		menu.setEnabled(b);
	}

	@Override
	public void registerHotSpotListener(IHotSpotListener hotSpotListener)
	{
		boolean b = hotSpotListener != this.hotSpotListener;

		this.hotSpotListener = hotSpotListener;
		this.menu.registerHotSpotListener(hotSpotListener);

		if (b)
			requestReloadSprites();
	}

	private List<Integer> getValidSelectedIndices()
	{
		List<Integer> indices = new ArrayList<Integer>();

		for (int i : list.getSelectedIndices())
			if (i >= 0 && i < list.getModel().getSize() - 1)
				indices.add(i);

		return indices;
	}

	private boolean isValidIndex(int index)
	{
		return index >= 0 && index < list.getModel().getSize();
	}

	private IPoint_r getSpriteSize()
	{
		return fixedSpriteSize != null ? fixedSpriteSize : tempSpriteSize;
	}

	/* *************** ISpriteList ************** */

	@Override
	public void loadAndDisplaySprites(SpriteNameGenerator spriteNameGenerator)
	{
		this.spriteNameGenerator = spriteNameGenerator;
		requestReloadSprites();
	}

	@Override
	public void setSpriteSize(IPoint_r spriteSize)
	{
		this.fixedSpriteSize = spriteSize;

		if (spriteSize != null)
		{
			BufferedImage img = new BufferedImage(spriteSize.getX(), spriteSize.getY(), BufferedImage.TYPE_INT_ARGB);
			newIcon = new ImageIcon(img);
		}
		else
			newIcon = null;
	}

	@Override
	public void clear()
	{
		DefaultListModel<SpriteCell> model = new DefaultListModel<SpriteCell>();
		list.setModel(model);
	}

	/* *************** ISpriteMenuListener ************** */

	@Override
	public void requestCloneSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			SpriteListControl.requestCloneSprite(spriteNameGenerator, index);
			requestReloadSprites();
		}
	}

	@Override
	public void requestInsertNewSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			SpriteListControl.requestInsertNewSprite(spriteNameGenerator, getSpriteSize(), index);
			requestReloadSprites();
		}
	}

	@Override
	public void requestInsertSpriteFromClipboard()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			SpriteListControl.requestInsertSpriteFromClipboard(spriteNameGenerator, null, index);
			requestReloadSprites();
		}
	}

	@Override
	public void requestSetSpriteHotspot()
	{
		if (hotSpotListener != null)
		{
			int index = list.getSelectedIndex();
			if (isValidIndex(index))
			{
				File f = spriteNameGenerator.getFile(index);
				try
				{
					hotSpotView.loadSprite(f, null /* TODO: pass hotspot list */);
					boolean b = hotSpotView.showDialog();

					if (b)
						hotSpotListener.setHotSpot(index, hotSpotView.getResult());
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(null, "There was a problem loading a sprite\n" + f.toString() + "\n\n" + e.toString(),
							"Set hotspot", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	@Override
	public void requestEditSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			File f = spriteNameGenerator.getFile(index);
			ExternalImageEditor.getInstance().execute(f);
		}
	}

	@Override
	public void requestDeleteSprites()
	{
		List<Integer> indices = getValidSelectedIndices();
		if (indices.size() > 0)
		{
			SpriteListControl.requestDeleteSprites(spriteNameGenerator, indices);
			requestReloadSprites();
		}
	}

	private ImageIcon createIcon(int spriteIndex)
	{
		ImageIcon icon;
		try
		{
			String filename = spriteNameGenerator.getFilename(spriteIndex);
			File f = spriteNameGenerator.getFile(filename);
			BufferedImage bi = ImageIO.read(f);

			if (hotSpotListener != null)
				SpriteDecorator.decorateSprite(bi, hotSpotListener.getHotSpotList(spriteIndex));

			icon = new ImageIcon(bi);
		}
		catch (Exception e)
		{
			icon = null;
		}
		return icon;
	}

	@Override
	public void requestReloadSprites()
	{
		DefaultListModel<SpriteCell> model = new DefaultListModel<SpriteCell>();
		boolean resume = true;
		Point t = null;
		int i = 0;

		if (spriteNameGenerator != null)
		{
			do
			{
				String filename = spriteNameGenerator.getFilename(i);
				File f = spriteNameGenerator.getFile(filename);
				resume = f.exists();

				if (resume)
				{
					// add an existing cell
					ExistingSpriteCell c = new ExistingSpriteCell(filename, i);
					ImageIcon icon = createIcon(i);
					c.setIcon(icon);
					if (t == null && icon != null)
						t = new Point(icon.getIconWidth(), icon.getIconHeight());

					model.addElement(c);
				}
				++i;
			}
			while (resume);
		}

		// memorize sprite size
		if (t != null)
			tempSpriteSize = t;

		// add an empty cell
		NewSpriteCell c = new NewSpriteCell(newIcon);
		model.addElement(c);

		list.setModel(model);
	}

	@Override
	public void requestCopySpriteImage()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			SpriteListControl.requestCopySpriteToClipboard(spriteNameGenerator, index);
			requestReloadSprites();
		}
	}

	@Override
	public void requestCopySpriteFilename()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			File file = spriteNameGenerator.getFile(index);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(file.toString()), null);
		}
	}

	@Override
	public void requestMakeTransparent()
	{
		List<Integer> indices = getValidSelectedIndices();
		if (indices.size() > 0)
		{
			SpriteListControl.requestMakeSpritesTransparent(spriteNameGenerator, indices);
			requestReloadSprites();
		}
	}

	/* *************** MouseListener ************** */

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON2)
			menu.requestHotSpot();
		else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
			requestEditSprite();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
}
