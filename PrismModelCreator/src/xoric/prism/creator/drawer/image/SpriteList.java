package xoric.prism.creator.drawer.image;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.SpriteNames;
import xoric.prism.creator.drawer.model.VariationList;
import xoric.prism.creator.drawer.view.PreviewFrame;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.world.entities.ViewAngle;

public class SpriteList extends JPanel implements MouseListener, ActionListener, ISpriteList, ISpriteMenuListener
{
	private static final long serialVersionUID = 1L;

	private final PreviewFrame previewFrame;

	private ImageIcon newIcon;

	private final JLabel hintLabel;
	private final JLabel hintLabel2;
	private final JButton previewButton;
	private final JList<SpriteCell> list;

	private final SpriteMenu menu;
	private IDrawerControl control;

	private IPoint_r spriteSize;
	private AnimationModel animationModel;
	private int variation;
	private ViewAngle viewAngle;

	public SpriteList()
	{
		super(new GridBagLayout());

		previewFrame = new PreviewFrame();

		hintLabel = new JLabel("Double click on a sprite in order to edit it.");
		hintLabel2 = new JLabel("Right click on the list below for further options.");

		list = new JList<SpriteCell>();
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(1);
		list.setCellRenderer(new SpriteCellRenderer());
		list.addMouseListener(this);

		previewButton = new JButton("Preview");
		previewButton.addActionListener(this);

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

		c = new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0);
		add(previewButton, c);

		c = new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(15, 0, 0, 0), 0, 0);
		add(scroll, c);
	}

	@Override
	public void loadFrames(VariationList list, int variation, ViewAngle v)
	{
		this.animationModel = list.getVariation(variation);
		this.variation = variation;
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
	public void setSpriteSize(IPoint_r spriteSize)
	{
		this.spriteSize = spriteSize;

		if (spriteSize != null)
		{
			BufferedImage img = new BufferedImage(spriteSize.getX(), spriteSize.getY(), BufferedImage.TYPE_INT_ARGB);
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
			control.requestCloneSprite(animationModel.getAnimationIndex(), variation, viewAngle, index);
	}

	@Override
	public void requestInsertSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
			control.requestInsertSprite(animationModel.getAnimationIndex(), variation, viewAngle, index);
	}

	@Override
	public void requestInsertSpriteFromClipboard()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
			control.requestInsertSpriteFromClipboard(animationModel.getAnimationIndex(), variation, viewAngle, index);
	}

	@Override
	public void requestEditSprite()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			String filename = SpriteNames.getFilename(animationModel.getAnimationIndex(), variation, viewAngle, index);
			File file = animationModel.getPath().getFile(filename);
			control.requestEditSprite(file);
		}
	}

	@Override
	public void requestDeleteSprites()
	{
		List<Integer> indices = getValidSelectedIndices();
		if (indices.size() > 0)
			control.requestDeleteSprites(animationModel.getAnimationIndex(), variation, viewAngle, indices);
	}

	@Override
	public void requestReloadSprite()
	{
		if (animationModel == null)
			return;

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
			control.requestCopySpriteToClipboard(animationModel.getAnimationIndex(), variation, viewAngle, index);
	}

	@Override
	public void requestCopySpriteFilename()
	{
		int index = list.getSelectedIndex();
		if (isValidIndex(index))
		{
			String filename = SpriteNames.getFilename(animationModel.getAnimationIndex(), variation, viewAngle, index);
			File file = animationModel.getPath().getFile(filename);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(file.toString()), null);
		}
	}

	@Override
	public void requestMakeTransparent()
	{
		List<Integer> indices = getValidSelectedIndices();
		if (indices.size() > 0)
			control.requestMakeSpritesTransparent(animationModel.getAnimationIndex(), variation, viewAngle, indices);
	}

	/* *************** MouseListener ************** */

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
			requestEditSprite();
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	/* ***************************** */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == previewButton)
		{
			previewFrame.loadAndPlay(animationModel, variation, viewAngle, spriteSize);
		}
	}
}
