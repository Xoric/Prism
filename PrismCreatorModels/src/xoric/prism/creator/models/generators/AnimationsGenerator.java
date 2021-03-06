package xoric.prism.creator.models.generators;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import xoric.prism.creator.models.model.ModelModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.IPath_r;
import xoric.prism.world.animations.AnimationIndex;
import xoric.prism.world.entities.ViewAngle;

public class AnimationsGenerator
{
	private final ModelModel model;
	private final int spriteWidth;
	private final int spriteHeight;

	public AnimationsGenerator(ModelModel model)
	{
		this.model = model;
		this.spriteWidth = model.getSpriteSize().getX();
		this.spriteHeight = model.getSpriteSize().getY();
	}

	public void generateAll()
	{
		try
		{
			// generate animations
			List<File> files = tryGenerate();

			// show success
			StringBuilder sb = new StringBuilder();

			if (files.size() == 0)
			{
				sb.append("No animation images were generated.");
			}
			else
			{
				sb.append("<html>The following animations have been generated:<br>");
				for (File f : files)
				{
					long s = f.length();
					sb.append("<br><code>" + f.getName() + "</code> - " + Common.getFileSize(s));
				}
				sb.append("</html>");
			}
			JOptionPane.showMessageDialog(null, sb.toString(), "Generate animations", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private List<File> tryGenerate() throws PrismException
	{
		// create model summary
		List<File> files = new ArrayList<File>();
		IPath_r path = model.getPath();
		AnimationListSummary als = new AnimationListSummary();
		als.load(path);

		// insert all sprites to the image
		for (int i = 0; i < als.getAnimationCount(); ++i)
		{
			AnimationSummary as = als.getAnimation(i);
			generateAnimation(as, files);
		}
		return files;
	}

	private void generateAnimation(AnimationSummary as, List<File> files) throws PrismException
	{
		for (VariationSummary vs : as.getVariations())
		{
			File f = generateVariation(vs);
			files.add(f);
		}
	}

	private File generateVariation(VariationSummary vs) throws PrismException
	{
		// get animation name
		AnimationIndex a = vs.getAnimationIndex();
		String variatioName = a.toString().toLowerCase() + vs.getVariation();
		AnimationMeta meta = new AnimationMeta();

		// calculate required width and height 
		int rows = vs.getAngleCount();
		int columns = vs.countMaxColumns();
		int width = columns * spriteWidth;
		int height = rows * spriteHeight;
		int spriteX = 0;
		int spriteY = 0;

		// create image to fit all sprites
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();

		for (int i = 0; i < vs.getAngleCount(); ++i)
		{
			AngleSummary an = vs.getAngle(i);
			ViewAngle v = an.getAngle();
			int n = an.getSpriteCount();
			meta.addAngle(v, n);

			for (int k = 0; k < n; ++k)
			{
				File file = an.getSpriteFile(k);
				try
				{
					BufferedImage bi = ImageIO.read(file);
					int x = spriteX + spriteWidth / 2 - bi.getWidth() / 2;
					int y = spriteY + spriteHeight - bi.getHeight();
					graphics.drawImage(bi, x, y, null);
					spriteX += spriteWidth;
				}
				catch (Exception e0)
				{
					PrismException e = new PrismException(e0);
					e.setText("There was an error while reading an image.");
					if (file != null)
						e.addInfo("image file", file.toString());
					throw e;
				}
			}
			spriteX = 0;
			spriteY += spriteHeight;
		}
		graphics.dispose();

		// write meta file
		File metaFile = model.getPath().getFile(variatioName + ".meta");
		try
		{
			FileOutputStream stream = new FileOutputStream(metaFile);
			meta.pack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was an error while writing a file.");
			e.addInfo("file", metaFile.toString());
			throw e;
		}

		// save image	
		File imageFile = model.getPath().getFile(variatioName + ".png");
		try
		{
			ImageIO.write(image, "png", imageFile);
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was an error while trying to write an image.");
			e.addInfo("image file", imageFile.toString());
			throw e;
		}
		return imageFile;
	}
}
