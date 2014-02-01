package xoric.prism.creator.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;
import xoric.prism.data.types.TextMap;

public class FontCreator
{
	private static final int width = 22;
	private static final int height = 22;
	private static final Path path = new Path("E:/Prism/work/ui/font");
	private static final Font font = new Font("Arimo", Font.BOLD, 20);
	private static final Font fontS = new Font("Arimo", Font.PLAIN, 16);

	private static final char[] smaller = { '%', '@' };

	public static void main(String args[])
	{
		//		listFonts();

		//		Font font = new Font("Arial", Font.PLAIN, 20.0f);

		try
		{
			for (int i = 0; i < 64; ++i)
				writeChar(i, font, path);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void writeChar(int i, Font font, IPath_r path) throws IOException
	{
		char c = TextMap.charOf(i);
		final int X = 1;
		final int Y = height - 6;
		System.out.println("writing '" + c + "'");

		BufferedImage letter = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; ++x)
			for (int y = 0; y < height; ++y)
				letter.setRGB(x, y, 0);

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setColor(Color.white);
		boolean b = true;
		for (char d : smaller)
			b &= c != d;
		g.setFont(b ? font : fontS);
		g.drawString(String.valueOf(c), X, Y);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		g.dispose();

		for (int x = 0; x < width; ++x)
		{
			for (int y = 0; y < height; ++y)
			{
				Color t = new Color(bi.getRGB(x, y));

				if (t.getRed() > 100)
					for (int ix = -1; ix < 2; ++ix)
						for (int iy = -1; iy < 2; ++iy)
							letter.setRGB(x + ix, y + iy, genBlack());
			}
		}

		for (int x = 0; x < width; ++x)
		{
			for (int y = 0; y < height; ++y)
			{
				Color t = new Color(bi.getRGB(x, y));

				if (t.getRed() > 100)
				{
					letter.setRGB(x, y, genGray());
					letter.setRGB(x + 1, y, genGray());
					letter.setRGB(x, y + 1, genGray());
					letter.setRGB(x + 1, y + 1, genGray());
				}
			}
		}

		for (int x = 0; x < width; ++x)
		{
			for (int y = 0; y < height; ++y)
			{
				Color t = new Color(bi.getRGB(x, y));

				if (t.getRed() > 100)
				{
					letter.setRGB(x, y, genWhite(y));
				}
			}
		}

		File f = path.getFile("sprite" + i + ".png");
		ImageIO.write(letter, "png", f);
	}

	private static int genWhite(int y)
	{
		int z;
		if (y < height / 2)
			z = y;
		else
			z = height / 2 - (y - height / 2);
		z = z * 6;

		int z0 = 160;

		int r = (int) (z0 + z + Math.random() * 20);
		int g = (int) (z0 + z + Math.random() * 20);
		int b = (int) (z0 + z + Math.random() * 20);
		Color c = new Color(r, g, b, 255);
		int white = c.getRGB();
		return white;
	}

	private static int genGray()
	{
		int r = (int) (130 + Math.random() * 20);
		int g = (int) (130 + Math.random() * 20);
		int b = (int) (150 + Math.random() * 20);
		Color c = new Color(r, g, b, 255);
		int white = c.getRGB();
		return white;
	}

	private static int genBlack()
	{
		int a = (int) (100 + Math.random() * 20);
		return a << 24;
	}

	private static void listFonts()
	{
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (String font : e.getAvailableFontFamilyNames())
		{
			System.out.println(font);
		}
	}
}
