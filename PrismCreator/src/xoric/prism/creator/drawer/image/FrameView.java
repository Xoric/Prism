package xoric.prism.creator.drawer.image;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xoric.prism.data.types.IPath_r;

public class FrameView extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JLabel iconLabel;
	private final JLabel nameLabel;

	public FrameView(int frameIndex)
	{
		super(new GridBagLayout());

		this.setBorder(BorderFactory.createEtchedBorder());

		iconLabel = new JLabel();
		nameLabel = new JLabel();

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
				2, 2, 0, 2), 0, 0);
		add(iconLabel, c);

		c = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0);
		add(nameLabel, c);
	}

	public void loadImage(IPath_r path, String filename)
	{
		nameLabel.setText("<html><span style=\"background-color:#DCDCDC; color:#0;\"><strong>" + filename.toLowerCase() + "</html>");

		try
		{
			File file = path.getFile(filename);
			Image img = ImageIO.read(file);
			ImageIcon icn = new ImageIcon(img);
			iconLabel.setIcon(icn);
		}
		catch (IOException e)
		{
			iconLabel.setIcon(null);
			iconLabel.setText(e.toString());
		}
	}
}
