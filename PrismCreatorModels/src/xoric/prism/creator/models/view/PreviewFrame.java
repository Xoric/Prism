package xoric.prism.creator.models.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import xoric.prism.creator.models.image.AnimationSpriteNameGenerator;
import xoric.prism.creator.models.model.AnimationModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.swing.PrismFrame;
import xoric.prism.swing.tooltips.ToolTipFormatter;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class PreviewFrame extends PrismFrame implements ActionListener, WindowListener
{
	private static final long serialVersionUID = 1L;

	private final JLabel imageLabel;

	private final JButton prevButton;
	private final JButton pauseButton;
	private final JButton playButton;
	private final JButton nextButton;

	private final JLabel titleLabel;
	private final JLabel indexLabel;

	private final Timer timer;

	private final List<ImageIcon> images;
	private int currentIndex = 0;

	public PreviewFrame()
	{
		super("Preview", 360, 300, false);

		addWindowListener(this);

		prevButton = createButton("Previous", "icons/prev.png", "Previous sprite");
		pauseButton = createButton("Pause", "icons/pause.png", "Pause animation");
		playButton = createButton("Play", "icons/play.png", "Play animation");
		nextButton = createButton("Next", "icons/next.png", "Next sprite");

		titleLabel = new JLabel();
		indexLabel = new JLabel();
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		indexLabel.setHorizontalAlignment(JLabel.CENTER);

		JPanel controlPanel = new JPanel(new FlowLayout());
		controlPanel.add(prevButton);
		controlPanel.add(pauseButton);
		controlPanel.add(playButton);
		controlPanel.add(nextButton);

		JPanel botPanel = new JPanel(new BorderLayout());
		botPanel.add(BorderLayout.NORTH, indexLabel);
		botPanel.add(BorderLayout.CENTER, controlPanel);

		imageLabel = new JLabel();
		imageLabel.setBorder(BorderFactory.createLineBorder(Color.black));

		images = new ArrayList<ImageIcon>();
		timer = new Timer(200, this);

		JPanel p = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
				20, 0, 0, 0), 0, 0);
		p.add(titleLabel, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0);
		p.add(imageLabel, c);

		c = new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0);
		p.add(botPanel, c);

		setContentPane(p);
		enableControls();
	}

	private void enableControls()
	{
		boolean b = timer.isRunning();

		pauseButton.setVisible(b);
		playButton.setVisible(!b);
	}

	private JButton createButton(String s, String icon, String tooltip)
	{
		JButton b = new JButton();
		b.addActionListener(this);
		boolean hasIcon = false;
		try
		{
			if (icon != null && icon.length() > 0)
			{
				Image img = ImageIO.read(ClassLoader.getSystemResource(icon));
				ImageIcon icn = new ImageIcon(img);
				b.setIcon(icn);
				Dimension d = new Dimension(icn.getIconWidth() + 8, icn.getIconHeight() + 8);
				b.setPreferredSize(d);
				b.setMinimumSize(d);
				b.setMaximumSize(d);
				b.setToolTipText(ToolTipFormatter.split(tooltip));
				hasIcon = true;
			}
		}
		catch (Exception e)
		{
		}
		if (!hasIcon)
			b.setText(s);

		return b;
	}

	public void loadAndPlay(AnimationModel m, int variation, ViewAngle v, IPoint_r spriteSize)
	{
		try
		{
			titleLabel.setText("<html><b>" + m.getAnimationIndex().toString() + "</b>." + v.toString().toLowerCase() + "</html>");

			loadImages(m, variation, v, spriteSize);
			int interval = calcInterval(m.getDurationMs(), images.size());
			showImage(0);
			currentIndex = 0;
			timer.setDelay(interval);
			timer.start();

			enableControls();
			setVisible(true);
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

	private void loadImages(AnimationModel m, int variation, ViewAngle v, IPoint_r spriteSize) throws PrismException
	{
		IPath_r path = m.getPath();
		AnimationIndex a = m.getAnimationIndex();

		images.clear();
		int w = spriteSize.getX();
		int h = spriteSize.getY();

		int n = 0;
		boolean b;
		do
		{
			String filename = AnimationSpriteNameGenerator.getFilename(a, variation, v, n);
			File file = path.getFile(filename);
			b = file.exists();
			if (b)
			{
				try
				{
					BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					Graphics graphics = image.getGraphics();

					BufferedImage bi = ImageIO.read(file);
					int x = w / 2 - bi.getWidth() / 2;
					int y = h - bi.getHeight();
					graphics.drawImage(bi, x, y, null);
					graphics.dispose();

					ImageIcon icon = new ImageIcon(image);
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
		indexLabel.setText("Sprite " + index + " of " + (images.size() - 1));
	}

	private void onTimerTick()
	{
		if (++currentIndex >= images.size())
			currentIndex = 0;

		showImage(currentIndex);
	}

	private void toggleTimer(boolean play)
	{
		if (play)
			timer.start();
		else
			timer.stop();

		enableControls();
	}

	private void onPreviousOrNext(int add)
	{
		timer.stop();
		enableControls();

		currentIndex += add;

		if (currentIndex < 0)
			currentIndex = images.size() - 1;
		else if (currentIndex >= images.size())
			currentIndex = 0;

		showImage(currentIndex);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();

		if (o == timer)
			onTimerTick();
		else if (o == pauseButton)
			toggleTimer(false);
		else if (o == playButton)
			toggleTimer(true);
		else if (o == nextButton)
			onPreviousOrNext(1);
		else if (o == prevButton)
			onPreviousOrNext(-1);
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
