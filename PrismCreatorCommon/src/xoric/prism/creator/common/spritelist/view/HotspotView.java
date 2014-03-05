package xoric.prism.creator.common.spritelist.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import xoric.prism.data.types.FloatPoint;

/**
 * @author XoricLee
 * @since 28.02.2014, 16:11:52
 */
public class HotspotView implements MouseListener, ActionListener
{
	private final JLabel imageLabel;

	private BufferedImage original;
	private BufferedImage decorated;

	private FloatPoint rightDown;

	private HotspotList result;
	private final FloatPoint hotSpotCopy;

	private final JButton copyHotSpotButton;
	private final JButton pasteHotSpotButton;

	private final JButton clearActionPointsButton;

	private final Object[] message;

	public HotspotView()
	{
		imageLabel = new JLabel();
		imageLabel.addMouseListener(this);

		result = new HotspotList();
		hotSpotCopy = new FloatPoint();

		JPanel q0 = new JPanel(new FlowLayout());
		q0.add(copyHotSpotButton = createButton("copy hotspot"));
		q0.add(pasteHotSpotButton = createButton("paste hotspot"));

		JPanel p0 = new JPanel(new BorderLayout());
		p0.add(BorderLayout.CENTER, createHint("<b>left-click</b> on the sprite to set its <font color=\"#FF0000\">hotspot</font>"));
		p0.add(BorderLayout.SOUTH, q0);

		JPanel q1 = new JPanel(new FlowLayout());
		q1.add(clearActionPointsButton = createButton("clear action points"));

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(BorderLayout.CENTER,
				createHint("<b>right-down + move mouse + right-up</b> will add an <font color=\"#0000FF\">action point</font>"));
		p1.add(BorderLayout.SOUTH, q1);

		message = new Object[] { null, null, imageLabel, p0, p1 };

		pasteHotSpotButton.setEnabled(false);
	}

	private JLabel createHint(String hint)
	{
		JLabel l = new JLabel("<html>" + hint + "</html>");
		return l;
	}

	private JButton createButton(String caption)
	{
		JButton b = new JButton(caption);
		b.addActionListener(this);
		return b;
	}

	public void loadSprite(File f, HotspotList list) throws IOException
	{
		original = ImageIO.read(f);

		if (list == null)
		{
			list = new HotspotList();
			list.hotspot.x = original.getWidth() / 2;
			list.hotspot.y = original.getHeight() - 2;
		}

		result = list;
		decorateImage();
		updateImage();
	}

	private void decorateImage()
	{
		decorated = new BufferedImage(original.getWidth(null), original.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = decorated.createGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();

		SpriteDecorator.decorateSprite(decorated, result);
	}

	private void updateImage()
	{
		ImageIcon icon = new ImageIcon(decorated);
		imageLabel.setIcon(icon);
	}

	public boolean showDialog()
	{
		int n = JOptionPane.showConfirmDialog(null, message, "Hotspot and action points", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		return n == JOptionPane.OK_OPTION;
	}

	// MouseListener:
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	// MouseListener:
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	// MouseListener:
	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	// MouseListener:
	@Override
	public void mousePressed(MouseEvent e)
	{
		int b = e.getButton();

		if (b == 1 || b == 3)
		{
			Point p = e.getPoint();

			if (b == MouseEvent.BUTTON1)
			{
				result.hotspot.x = p.x;
				result.hotspot.y = p.y;
				decorateImage();
				updateImage();
			}
			else
			{
				rightDown = new FloatPoint(p.x, p.y);
			}
		}
	}

	// MouseListener:
	@Override
	public void mouseReleased(MouseEvent e)
	{
		int b = e.getButton();

		if (rightDown != null && b == 3)
		{
			Point p = e.getPoint();
			FloatPoint rightUp = new FloatPoint(p.x, p.y);

			if (!rightDown.equals(rightUp))
			{
				ActionPoint ap = new ActionPoint(rightDown, rightUp);
				result.actionPoints.add(ap);

				rightDown = null;

				decorateImage();
				updateImage();
			}
		}
	}

	// ActionListener:
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == clearActionPointsButton)
		{
			result.actionPoints.clear();
			decorateImage();
			updateImage();
		}
		else if (o == copyHotSpotButton)
		{
			hotSpotCopy.copyFrom(result.hotspot);
			pasteHotSpotButton.setEnabled(true);
		}
		else if (o == pasteHotSpotButton)
		{
			result.hotspot.copyFrom(hotSpotCopy);
			decorateImage();
			updateImage();
		}
	}

	public HotspotList getResult()
	{
		return result;
	}
}
