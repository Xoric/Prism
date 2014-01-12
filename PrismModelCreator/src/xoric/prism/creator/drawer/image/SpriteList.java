package xoric.prism.creator.drawer.image;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.SpriteNames;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.entities.ViewAngle;

public class SpriteList extends JPanel implements ISpriteList, ISpriteMenuListener
{
	private static final long serialVersionUID = 1L;

	private ImageIcon newIcon;

	private final JLabel hintLabel;
	private final JList<SpriteCell> list;

	private final SpriteMenu menu;
	private IDrawerControl control;

	private AnimationModel animationModel;
	private ViewAngle viewAngle;

	public SpriteList()
	{
		super(new GridBagLayout());

		hintLabel = new JLabel("Right click on the list below for further options.");

		list = new JList<SpriteCell>();
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(1);
		list.setCellRenderer(new SpriteCellRenderer());

		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		menu = new SpriteMenu(this);
		list.setComponentPopupMenu(menu);

		Insets insets = new Insets(0, 0, 0, 0);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(hintLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(scroll, c);
	}

	@Override
	public void loadFrames(AnimationModel m, ViewAngle v)
	{
		this.animationModel = m;
		this.viewAngle = v;

		requestReloadSprite();
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

	/* *************** ISpriteList ************** */

	@Override
	public void setControl(IDrawerControl control)
	{
		this.control = control;
	}

	@Override
	public void setTileSize(IPoint_r tileSize)
	{
		if (tileSize != null)
		{
			BufferedImage img = new BufferedImage(tileSize.getX(), tileSize.getY(), BufferedImage.TYPE_INT_ARGB);
			newIcon = new ImageIcon(img);
		}
		else
			newIcon = null;
	}

	/* *************** ISpriteMenuListener ************** */

	@Override
	public void requestCloneSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
			control.requestCloneSprite(animationModel.getAnimationIndex(), viewAngle, index);
	}

	@Override
	public void requestInsertSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
			control.requestInsertSprite(animationModel.getAnimationIndex(), viewAngle, index);
	}

	@Override
	public void requestInsertSpriteFromClipboard()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
			control.requestInsertSpriteFromClipboard(animationModel.getAnimationIndex(), viewAngle, index);
	}

	@Override
	public void requestEditSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			String filename = SpriteNames.getFilename(animationModel.getAnimationIndex(), viewAngle, index);
			File file = animationModel.getPath().getFile(filename);
			control.requestEditSprite(file);
		}
	}

	@Override
	public void requestDeleteSprites()
	{
		List<Integer> indices = getValidSelectedIndices();
		if (indices.size() > 0)
			control.requestDeleteSprites(animationModel.getAnimationIndex(), viewAngle, indices);
	}

	@Override
	public void requestReloadSprite()
	{
		DefaultListModel<SpriteCell> model = new DefaultListModel<SpriteCell>();
		boolean resume = true;
		int i = 0;

		do
		{
			String filename = animationModel.getFilename(viewAngle, i);
			IPath_r path = animationModel.getPath();
			File f = path.getFile(filename);
			resume = f.exists();

			if (resume)
			{
				// add an existing cell
				ExistingSpriteCell c = new ExistingSpriteCell(filename, i);
				c.loadIcon(path);
				model.addElement(c);
			}
			++i;
		}
		while (resume);

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
			control.requestCopySpriteToClipboard(animationModel.getAnimationIndex(), viewAngle, index);
	}

	@Override
	public void requestCopySpriteFilename()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			String filename = SpriteNames.getFilename(animationModel.getAnimationIndex(), viewAngle, index);
			File file = animationModel.getPath().getFile(filename);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(file.toString()), null);
		}
	}

	/* ***************************** */
}