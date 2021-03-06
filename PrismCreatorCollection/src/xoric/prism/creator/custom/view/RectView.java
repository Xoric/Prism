package xoric.prism.creator.custom.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xoric.prism.creator.common.buttonpanel.ButtonPanel;
import xoric.prism.creator.common.buttonpanel.IButtonPanel;
import xoric.prism.creator.common.buttonpanel.IButtonPanelListener;
import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.custom.model.ObjectModel;
import xoric.prism.data.types.Rect;
import xoric.prism.swing.PrismPanel;
import xoric.prism.swing.PrismSwing;

public class RectView extends JPanel implements ListSelectionListener, ChangeListener, MouseListener, MouseMotionListener, IRectView,
		IButtonPanelListener
{
	private static final long serialVersionUID = 1L;
	private static final Color fill0 = new Color(0, 0, 0, 50);
	private static final Color border0 = new Color(0, 0, 0, 255);
	private static final Color fill1 = new Color(Color.orange.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 50);
	private static final Color border1 = new Color(Color.orange.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 255);

	private IRectControl control;

	private ObjectModel model;
	private DefaultListModel<String> listModel;

	private final JLabel imageLabel;
	private boolean isMouseDown;

	private BufferedImage image;
	private BufferedImage scaledImage;
	private BufferedImage decoratedImage;
	private JLabel zoomLabel;
	private JSlider slider;

	private JList<String> rectList;
	private IButtonPanel buttonPanel;

	public RectView()
	{
		super(new GridBagLayout());

		imageLabel = new JLabel();
		imageLabel.addMouseListener(this);
		imageLabel.addMouseMotionListener(this);
		imageLabel.setOpaque(true);
		imageLabel.setBackground(Color.red);
		JPanel p = new JPanel();
		p.add(imageLabel);
		JScrollPane scroll = PrismSwing.createScrollPane(p);

		rectList = new JList<String>();
		listModel = new DefaultListModel<String>();
		rectList.setModel(listModel);
		rectList.addListSelectionListener(this);
		JScrollPane scroll2 = PrismSwing.createScrollPane(rectList);
		PrismPanel p2 = new PrismPanel("Rectangles");
		p2.setContent(scroll2);

		ButtonPanel bp = new ButtonPanel(this, "rectangle", true, false, true);
		buttonPanel = bp;

		JPanel p3 = new JPanel(new BorderLayout());
		p3.add(BorderLayout.CENTER, p2);
		p3.add(BorderLayout.SOUTH, bp);

		JPanel zoomPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.3, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 0, 0), 0, 0);
		zoomPanel.add(zoomLabel = new JLabel("Zoom"), c);
		c = new GridBagConstraints(1, 0, 1, 1, 0.7, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		zoomPanel.add(slider = new JSlider(), c);
		slider.setMinimum(1);
		slider.setMaximum(15);
		slider.addChangeListener(this);

		c = new GridBagConstraints(0, 0, 1, 1, 0.2, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 15), 0, 0);
		add(p3, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.85, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		add(scroll, c);

		c.weighty = 0.0;
		c.gridy = 1;
		add(zoomPanel, c);

		enableControls();
	}

	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		enableControls();
	}

	@Override
	public void enableControls()
	{
		boolean b = this.isEnabled() && image != null;
		slider.setEnabled(b);
		enableButtons();
	}

	private void enableButtons()
	{
		buttonPanel.setEnabled(image != null, rectList.getSelectedIndex() >= 0);
	}

	@Override
	public void setControl(IRectControl control)
	{
		this.control = control;
	}

	private void loadImage(File file)
	{
		scaledImage = null;
		decoratedImage = null;

		try
		{
			image = ImageIO.read(file);
		}
		catch (IOException e)
		{
			image = null;
		}
	}

	private void scaleImage()
	{
		if (image == null)
		{
			scaledImage = null;
		}
		else
		{
			int zoom = slider.getValue();
			int w = image.getWidth() * zoom;
			int h = image.getHeight() * zoom;

			scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g = scaledImage.getGraphics();
			g.drawImage(image, 0, 0, w, h, null);
			g.dispose();
		}
	}

	private void decorateScaledImage()
	{
		if (scaledImage == null)
		{
			decoratedImage = null;
		}
		else
		{
			decoratedImage = new BufferedImage(scaledImage.getWidth(), scaledImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = decoratedImage.getGraphics();
			g.drawImage(scaledImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);

			int selectedIndex = rectList.getSelectedIndex();
			final int n = model.getRectCount();
			int zoom = slider.getValue();

			for (int i = 0; i < n; ++i)
			{
				Rect r = model.getRect(i);

				if (r.getWidth() > 0 && r.getHeight() > 0)
				{
					int sx = r.getX() * zoom;
					int sy = r.getY() * zoom;
					int w = r.getWidth() * zoom - 1;
					int h = r.getHeight() * zoom - 1;

					boolean b = selectedIndex == i;
					g.setColor(b ? fill1 : fill0);
					g.fillRect(sx, sy, w, h);
					g.setColor(b ? border1 : border0);
					g.drawRect(sx, sy, w, h);
				}
			}
		}
	}

	private void showDecoratedImage()
	{
		if (decoratedImage != null)
		{
			ImageIcon icon = new ImageIcon(decoratedImage);
			imageLabel.setIcon(icon);
			imageLabel.setText("");
		}
		else
		{
			imageLabel.setIcon(null);
			imageLabel.setText("Error");
		}
		imageLabel.revalidate();
	}

	private void updateList()
	{
		if (model == null)
		{
			listModel.clear();
		}
		else
		{
			final int n = model.getRectCount();
			int sel = rectList.getSelectedIndex();

			for (int i = 0; i < n; ++i)
			{
				String s = "Rect " + i + ": " + model.getRect(i).toString();

				if (i < listModel.size())
					listModel.set(i, s);
				else
					listModel.addElement(s);
			}

			for (int i = n; i < listModel.size(); ++i)
				listModel.remove(i);

			if (sel >= listModel.size())
				sel = listModel.size() - 1;

			if (sel >= 0 && sel < listModel.size())
			{
				rectList.setSelectedIndex(sel);
				valueChanged(null);
			}
		}
	}

	@Override
	public void displayObject(ObjectModel model, SpriteNameGenerator spriteNameGenerator)
	{
		this.model = model;
		clear();

		loadImage(spriteNameGenerator.getFile(0));

		updateList();

		loadImage(spriteNameGenerator.getFile(0));
		scaleImage();
		decorateScaledImage();
		showDecoratedImage();

		enableControls();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		decorateScaledImage();
		showDecoratedImage();

		buttonPanel.setEnabled(true, rectList.getSelectedIndex() >= 0);
	}

	@Override
	public void clear()
	{
		listModel.clear();
		imageLabel.setIcon(null);
		imageLabel.revalidate();

		image = null;
		scaledImage = null;
		decoratedImage = null;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == slider)
		{
			scaleImage();
			decorateScaledImage();
			showDecoratedImage();

			zoomLabel.setText("Zoom " + slider.getValue() + "x");
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
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
	public void mousePressed(MouseEvent e)
	{
		isMouseDown = true;

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			int index = rectList.getSelectedIndex();

			if (index >= 0 && index < rectList.getModel().getSize())
			{
				Rect r = model.getRect(index);
				int zoom = slider.getValue();
				int x = e.getX() / zoom;
				int y = e.getY() / zoom;

				if (x != r.getX() || y != r.getY())
				{
					int dx = x - r.getX();
					int dy = y - r.getY();
					r.setPosition(x, y);

					int w = r.getWidth() - dx;
					int h = r.getHeight() - dy;
					if (w < 0)
						w = 0;
					if (h < 0)
						h = 0;
					r.setSize(w, h);

					decorateScaledImage();
					showDecoratedImage();

					updateList();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		isMouseDown = false;

		control.requestSaveCollection();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (isMouseDown)
		{
			int index = rectList.getSelectedIndex();

			if (index >= 0 && index < rectList.getModel().getSize())
			{
				Rect r = model.getRect(index);
				int zoom = slider.getValue();

				int x = e.getX() / zoom;
				int y = e.getY() / zoom;

				int w = x - r.getX() + 1;
				int h = y - r.getY() + 1;

				if (w < 0)
					w = 0;
				else if (r.getX() + w > image.getWidth())
					w = image.getWidth() - r.getX();

				if (h < 0)
					h = 0;
				else if (r.getY() + h > image.getHeight())
					h = image.getHeight() - r.getY();

				if (w != r.getWidth() || h != r.getHeight())
				{
					r.setSize(w, h);

					decorateScaledImage();
					showDecoratedImage();

					updateList();
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
	}

	@Override
	public void onAddButton()
	{
		if (model != null)
		{
			Rect r = new Rect();
			model.addRect(r);
			updateList();
			rectList.setSelectedIndex(listModel.size() - 1);

			decorateScaledImage();
			showDecoratedImage();

			control.requestSaveCollection();
		}
	}

	@Override
	public void onEditButton()
	{
	}

	@Override
	public void onDeleteButton()
	{
		if (model != null)
		{
			int index = rectList.getSelectedIndex();

			if (index >= 0 && index < listModel.size())
			{
				model.removeRect(index);

				updateList();

				decorateScaledImage();
				showDecoratedImage();

				control.requestSaveCollection();
			}
		}
	}

	@Override
	public void onUpButton()
	{
	}

	@Override
	public void onDownButton()
	{
	}
}
