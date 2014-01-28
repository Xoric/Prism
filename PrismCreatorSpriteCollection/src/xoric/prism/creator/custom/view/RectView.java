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
	private static final int rgb0 = Color.black.getRGB();
	private static final int rgb1 = Color.orange.getRGB();

	private IRectControl control;

	private ObjectModel model;
	private DefaultListModel<String> listModel;

	private final JLabel imageLabel;
	private boolean isMouseDown;

	private BufferedImage image;
	private BufferedImage decoratedImage;
	private JSlider slider;

	private JList<String> rectList;
	private IButtonPanel buttonPanel;

	private boolean ignoreListSelections;

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

		JPanel zoomPanel = new JPanel(new BorderLayout());
		zoomPanel.add(BorderLayout.WEST, new JLabel("Zoom"));
		zoomPanel.add(BorderLayout.CENTER, slider = new JSlider());
		slider.setMinimum(1);
		slider.setMaximum(15);
		slider.addChangeListener(this);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 2, 0.3, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 0, 15), 0, 0);
		add(p3, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.7, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		add(scroll, c);

		c.weighty = 0.0;
		c.gridy = 1;
		add(zoomPanel, c);

		enableControls();
	}

	private void enableControls()
	{
		boolean b = this.isEnabled() && image != null;
		slider.setEnabled(b);
		enableButtons();
	}

	private void enableButtons()
	{
		int n = rectList.getSelectedIndices().length;
		buttonPanel.setEnabled(this.isEnabled(), n > 0);
	}

	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		enableControls();
	}

	@Override
	public void setControl(IRectControl control)
	{
		this.control = control;
	}

	private void decorateImage()
	{
		decoratedImage = deepCopy();

		if (decoratedImage == null)
			return;

		int selectedIndex = rectList.getSelectedIndex();
		final int n = model.getRectCount();

		for (int i = 0; i < n; ++i)
		{
			Rect r = model.getRect(i);
			int rgb = selectedIndex == i ? rgb1 : rgb0;

			if (r.getWidth() > 0 && r.getHeight() > 0)
			{
				int zoom = slider.getValue();

				int sx = r.getX() * zoom;
				int sy = r.getY() * zoom;
				int w = r.getWidth() * zoom;
				int h = r.getHeight() * zoom;
				int sx2 = sx + w + zoom - 1;
				int sy2 = sy + h + zoom - 1;

				for (int x = sx; x < sx + w + zoom; ++x)
				{
					decoratedImage.setRGB(x, sy, rgb);
					decoratedImage.setRGB(x, sy2, rgb);
				}
				for (int y = sy; y < sy + h + zoom; ++y)
				{
					decoratedImage.setRGB(sx, y, rgb);
					decoratedImage.setRGB(sx2, y, rgb);
				}
			}
		}
	}

	private void updateImage()
	{
		decorateImage();

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

	private BufferedImage deepCopy()
	{
		BufferedImage bi;

		if (image != null)
		{
			int zoom = slider.getValue();
			int w = image.getWidth() * zoom;
			int h = image.getHeight() * zoom;

			bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.getGraphics();
			g.drawImage(image, 0, 0, w, h, null);
			g.dispose();
		}
		else
			bi = null;

		return bi;
	}

	private void loadImage(File file)
	{
		try
		{
			image = ImageIO.read(file);
			slider.setEnabled(true);
		}
		catch (IOException e)
		{
			image = null;
			slider.setEnabled(false);
		}
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
		}
	}

	@Override
	public void displayObject(ObjectModel model, SpriteNameGenerator spriteNameGenerator)
	{
		this.model = model;
		clear();

		loadImage(spriteNameGenerator.getFile(0));

		updateList();

		updateImage();
		enableControls();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		updateImage();
	}

	@Override
	public void clear()
	{
		listModel.clear();
		imageLabel.setIcon(null);
		imageLabel.revalidate();
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == slider)
			updateImage();
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

					updateImage();
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
				else if (r.getX() + w >= image.getWidth())
					w = image.getWidth() - r.getX() - 1;

				if (h < 0)
					h = 0;
				else if (r.getY() + h >= image.getHeight())
					h = image.getHeight() - r.getY() - 1;

				if (w != r.getWidth() || h != r.getHeight())
				{
					r.setSize(w, h);

					updateImage();
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
			updateImage();

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
				listModel.remove(index);
				updateList();
				updateImage();

				control.requestSaveCollection();
			}
		}
	}
}