package xoric.prism.creator.custom.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xoric.prism.creator.custom.model.SpriteModel;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;

public class SpriteList extends JPanel implements MouseListener, ActionListener
{
	private static final long serialVersionUID = 1L;

	private ImageIcon newIcon;

	private final JLabel hintLabel;
	private final JLabel hintLabel2;
	private final JList<SpriteCell> list;

	private IPoint_r spriteSize;
	private int variation;

	public SpriteList()
	{
		super(new GridBagLayout());

		hintLabel = new JLabel("Double click on a sprite in order to edit it.");
		hintLabel2 = new JLabel("Right click on the list below for further options.");

		list = new JList<SpriteCell>();
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(1);
		list.setCellRenderer(new SpriteCellRenderer());
		list.addMouseListener(this);

		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		//		list.setComponentPopupMenu(menu);

		Insets insets = new Insets(0, 0, 0, 0);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(hintLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0);
		add(hintLabel2, c);

		c = new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(15, 0, 0, 0), 0, 0);
		add(scroll, c);
	}

	public void display(IPath_r path, List<SpriteModel> spriteModels)
	{
		DefaultListModel<SpriteCell> model = new DefaultListModel<SpriteCell>();

		for (int i = 0; i < spriteModels.size(); ++i)
		{
			SpriteModel m = spriteModels.get(i);
			String filename = m.getFilename();

			// add cell
			ExistingSpriteCell c = new ExistingSpriteCell(filename, i);
			c.loadIcon(path);
			model.addElement(c);
		}
		list.setModel(model);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}
}
