package xoric.prism.creator.drawer.image;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.entities.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class SpriteList extends JPanel implements ActionListener, ISpriteList, ISpriteMenuListener
{
	private static final long serialVersionUID = 1L;

	private final JList<SpriteCell> list;

	private final SpriteMenu menu;
	private IDrawerControl control;

	private AnimationIndex animation;
	private ViewAngle viewAngle;

	public SpriteList()
	{
		super(new GridBagLayout());

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
		add(scroll, c);
	}

	@Override
	public void loadFrames(AnimationModel m, ViewAngle v)
	{
		this.animation = m.getAnimationIndex();
		this.viewAngle = v;

		DefaultListModel<SpriteCell> model = new DefaultListModel<SpriteCell>();
		boolean resume = true;
		int i = 0;

		do
		{
			String filename = m.getFileName(v, i);
			IPath_r path = m.getPath();
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
		NewSpriteCell c = new NewSpriteCell();
		model.addElement(c);

		list.setModel(model);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		//		spriteMenu.sh
		int x = 200;
		int y = 200;
		menu.show(this, x, y);
	}

	/* *************** ISpriteList ************** */

	@Override
	public void setControl(IDrawerControl control)
	{
		this.control = control;
	}

	/* *************** ISpriteMenuListener ************** */

	@Override
	public void requestInsertSprite()
	{
		int index = list.getSelectedIndex();
		control.requestAddSprite(animation, viewAngle, index);
	}

	@Override
	public void requestDeleteItem()
	{
		// TODO Auto-generated method stub

	}

	/* ***************************** */
}
