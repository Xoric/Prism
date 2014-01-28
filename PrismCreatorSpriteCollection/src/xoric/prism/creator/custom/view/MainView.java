package xoric.prism.creator.custom.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.common.spritelist.view.ISpriteList;
import xoric.prism.creator.common.spritelist.view.SpriteList;
import xoric.prism.creator.common.view.PrismCreatorCommonView;
import xoric.prism.creator.custom.SpriteCollectionSpriteNameGenerator;
import xoric.prism.creator.custom.control.IMainControl;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.creator.custom.model.SpriteCollectionModel;

public class MainView extends PrismCreatorCommonView implements ActionListener, ISpriteCollectionView, IObjectListListener
{
	private static final long serialVersionUID = 1L;

	private IMainControl control;

	private SpriteCollectionModel model;

	private final IObjectList objectList;
	private final ISpriteList spriteList;
	private final IRectView rectView;

	private final JMenuItem mnuItemCreateTexture;

	public MainView()
	{
		super("SpriteCollection");
		super.setLayout(new GridBagLayout());

		ObjectList o = new ObjectList(this);
		objectList = o;
		Insets insets = new Insets(30, 30, 30, 30);
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 2, 0.4, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
				0);
		add(o, c);

		SpriteList s = new SpriteList();
		spriteList = s;
		insets = new Insets(30, 0, 30, 30);
		c = new GridBagConstraints(1, 1, 1, 1, 0.6, 0.4, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(s, c);

		RectView r = new RectView();
		rectView = r;
		c = new GridBagConstraints(1, 0, 1, 1, 0.6, 0.6, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(r, c);

		mnuItemCreateTexture = new JMenuItem("Combined texture (.png)");

		mnuItemCreateTexture.addActionListener(this);

		super.getMainMenuBar().addCreationItem(mnuItemCreateTexture);

		setModel(null);
	}

	public void start()
	{
		setVisible(true);
	}

	@Override
	public void setControl(IMainControl control)
	{
		super.setMainMenuListener(control);
		this.control = control;
		this.objectList.setControl(control);
		this.rectView.setControl(control);
	}

	@Override
	public void setModel(SpriteCollectionModel model)
	{
		this.model = model;
		this.objectList.setModel(model);

		boolean b = model != null;
		objectList.setEnabled(b);
		spriteList.setEnabled(b);
		rectView.setEnabled(b);
	}

	@Override
	public void displayAll()
	{
		displayObjects();
		displayObject();
	}

	@Override
	public void displayObjects()
	{
		objectList.displayObjects();
	}

	private void displaySprites(SpriteNameGenerator spriteNameGenerator)
	{
		spriteList.loadAndDisplaySprites(spriteNameGenerator);
		revalidate();
		repaint();
	}

	private void displayRects(ObjectModel model, SpriteNameGenerator spriteNameGenerator)
	{
		rectView.displayObject(model, spriteNameGenerator);
	}

	@Override
	public void displayObject()
	{
		int index = objectList.getSelectedIndex();

		if (this.model != null && index >= 0 && index < this.model.getCount())
		{
			ObjectModel model = this.model.getObjectModel(index);
			SpriteNameGenerator spriteNameGenerator = new SpriteCollectionSpriteNameGenerator(this.model.getPath(), model.getName());

			displaySprites(spriteNameGenerator);
			displayRects(model, spriteNameGenerator);
		}
		else
		{
			spriteList.clear();
			rectView.clear();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == mnuItemCreateTexture)
			control.requestCreateTexture();
	}
}