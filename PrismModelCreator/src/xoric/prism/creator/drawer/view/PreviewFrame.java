package xoric.prism.creator.drawer.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import xoric.prism.creator.drawer.model.AnimationModel;
import xoric.prism.creator.drawer.model.SpriteNames;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.swing.PrismFrame;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class PreviewFrame extends PrismFrame implements ActionListener, WindowListener
{
	private static final long serialVersionUID = 1L;

	private final JLabel imageLabel;
	private final Timer timer;

	private final List<ImageIcon> images;
	private int currentIndex = 0;

	public PreviewFrame()
	{
		super("Preview", 360, 300, false);

		addWindowListener(this);

		imageLabel = new JLabel();
		images = new ArrayList<ImageIcon>();
		timer = new Timer(200, this);

		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
				0, 0, 0, 0), 0, 0);
		p.add(imageLabel, c);

		setContentPane(p);
	}

	public void loadAndPlay(AnimationModel m, ViewAngle v)
	{
		AnimationIndex a = m.getAnimationIndex();
		IPath_r path = m.getPath();

		try
		{
			loadImages(path, a, v);
			int interval = calcInterval(m.getDurationMs(), images.size());
			showImage(0);
			currentIndex = 0;
			timer.setDelay(interval);
			timer.start();

			this.setVisible(true);
		}
		catch (PrismException e)
		{
			e.user.showMessage();
		}
	}

	private int calcInterval(int durationMs, int sprites) throws PrismException
	{
		int interval = 0;
		try
		{
			interval = durationMs / sprites;
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("Invalid interval.");
			throw e;
		}
		return interval;
	}

	private void loadImages(IPath_r path, AnimationIndex a, ViewAngle v) throws PrismException
	{
		images.clear();

		int n = 0;
		boolean b;
		do
		{
			String filename = SpriteNames.getFilename(a, v, n);
			File file = path.getFile(filename);
			b = file.exists();
			if (b)
			{
				BufferedImage bi;
				try
				{
					bi = ImageIO.read(file);
					ImageIcon icon = new ImageIcon(bi);
					images.add(icon);
				}
				catch (Exception e0)
				{
					PrismException e = new PrismException(e0);
					e.setText("There was a problem loading an image.");
					e.addInfo("image file", file.toString());
					throw e;
				}
				++n;
			}
		}
		while (b);

		if (images.size() == 0)
		{
			PrismException e = new PrismException();
			e.setText("No images were found.");
			throw e;
		}
	}

	private void showImage(int index)
	{
		ImageIcon icon = images.get(index);
		imageLabel.setIcon(icon);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == timer)
		{
			if (++currentIndex >= images.size())
				currentIndex = 0;

			showImage(currentIndex);
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		timer.stop();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
	}
}
