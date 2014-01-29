package xoric.prism.creator.custom.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.common.spritelist.view.ISpriteList;
import xoric.prism.creator.common.spritelist.view.SpriteList;
import xoric.prism.creator.common.view.IMainMenuBar;
import xoric.prism.creator.common.view.PrismCreatorCommonView;
import xoric.prism.creator.custom.control.IMainControl;
import xoric.prism.creator.custom.control.SpriteCollectionSpriteNameGenerator;
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
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 2, 0.15, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
				0);
		add(o, c);

		SpriteList s = new SpriteList();
		spriteList = s;
		insets = new Insets(30, 0, 30, 30);
		c = new GridBagConstraints(1, 1, 1, 1, 0.85, 0.4, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(s, c);

		RectView r = new RectView();
		rectView = r;
		c = new GridBagConstraints(1, 0, 1, 1, 0.85, 0.6, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(r, c);

		mnuItemCreateTexture = new JMenuItem("Combined texture (.png)");

		mnuItemCreateTexture.addActionListener(this);

		IMainMenuBar m = super.getMainMenuBar();
		m.addCreationItem(mnuItemCreateTexture);

		appendAboutInfo();

		setModel(null);
	}

	private void appendAboutInfo()
	{
		IMainMenuBar m = super.getMainMenuBar();

		String s = "http://www.java2s.com/Code/Java/2D-Graphics-GUI/Triestopackrectanglesastightlyaspossible.htm";
		JLabel l1 = new JLabel(
				"<html><p style=\"width:440pt;\">This program uses the <b>RectanglePacker</b> class developed by Ryan McNally to pack multiple sprites into a common texture.</p></html>");

		JTextPane e = new JTextPane();
		e.setEditable(false);
		e.setText(s);
		e.setFont(e.getFont().deriveFont(9.0f));
		JPanel p = new JPanel(new BorderLayout());
		p.add(BorderLayout.WEST, new JLabel("Source: "));
		p.add(BorderLayout.CENTER, e);

		JTextPane f = new JTextPane();
		f.setEditable(false);
		f.setText("Copyright (c) 2007, Ryan McNally All rights reserved. Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. Neither the name of the ORGANIZATION nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
		JScrollPane u = new JScrollPane(f);
		u.setPreferredSize(new Dimension(300, 200));

		m.appendAboutComponent(new JSeparator());
		m.appendAboutComponent(l1);
		m.appendAboutComponent(p);
		m.appendAboutComponent(u);
	}

	public void start()
	{
		setVisible(true);
	}

	@Override
	public void setControl(IMainControl control)
	{
		super.getMainMenuBar().setMainMenuListener(control);
		this.control = control;
		this.objectList.setControl(control);
		this.rectView.setControl(control);
	}

	@Override
	public void setModel(SpriteCollectionModel model)
	{
		this.model = model;
		this.objectList.setModel(model);
		super.getMainMenuBar().setModelObjectIsNull(model == null);

		boolean b = model != null;
		objectList.setEnabled(b);
		spriteList.setEnabled(b);
		rectView.enableControls();
		//		rectView.setEnabled(b && objectList.getSelectedIndex() >= 0);
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
