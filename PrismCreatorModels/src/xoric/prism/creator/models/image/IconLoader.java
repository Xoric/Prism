package xoric.prism.creator.models.image;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public abstract class IconLoader
{
	public static ImageIcon loadIconFromFile(File file) throws Exception
	{
		ImageIcon icon = new ImageIcon(file.toString());
		return icon;
	}

	public static ImageIcon loadIconFromFile(File file, int maxWidth, int maxHeight) throws Exception
	{
		ImageIcon icon = new ImageIcon(file.toString());
		icon = scaleIcon(icon, maxWidth, maxHeight);
		return icon;
	}

	public static ImageIcon loadIconFromResource(String resource) throws Exception
	{
		Image img = ImageIO.read(ClassLoader.getSystemResource(resource));
		ImageIcon icon = new ImageIcon(img);
		return icon;
	}

	public static ImageIcon loadIconFromResource(String resource, int maxWidth, int maxHeight) throws Exception
	{
		ImageIcon icon = loadIconFromResource(resource);
		icon = scaleIcon(icon, maxWidth, maxHeight);
		return icon;
	}

	private static ImageIcon scaleIcon(ImageIcon icon, int maxWidth, int maxHeight)
	{
		// scale icon if too big
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();

		if (w > maxWidth || h > maxHeight)
		{
			float f1 = (float) maxWidth / w;
			float f2 = (float) maxHeight / h;
			float f = f1 < f2 ? f1 : f2;
			w *= f;
			h *= f;

			Image image = icon.getImage();
			Image newimg = image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(newimg);
		}
		return icon;
	}
}
