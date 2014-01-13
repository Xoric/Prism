package xoric.prism.creator.drawer.generator;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Text;
import xoric.prism.develop.meta.MetaFileCreator;
import xoric.prism.world.entities.AnimationIndex;

public class ModelGenerator
{
	private final DrawerModel model;

	public ModelGenerator(DrawerModel model)
	{
		this.model = model;
	}

	public void generateModel(String targetFilename)
	{
		try
		{
			// generate model
			List<AnimationIndex> addedAnimations = tryGenerate(targetFilename);

			// show success
			showSuccess(addedAnimations, targetFilename);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private void showSuccess(List<AnimationIndex> addedAnimations, String targetFilename)
	{
		// generate message
		StringBuilder sb = new StringBuilder();

		sb.append("<html>The model <code>" + targetFilename + "</code> was created successfully.<br>");
		sb.append("The following animations were added:<br><br>");
		int n = 0;
		for (int i = 0; i < addedAnimations.size(); ++i)
		{
			if (++n >= 5)
			{
				n = 0;
				sb.append("<br>");
			}
			else if (i > 0)
				sb.append(", ");

			sb.append("<code>" + addedAnimations.get(i).toString() + "</code>");
		}
		sb.append("</html>");

		// load the model's portrait as icon if any
		ImageIcon icon = null;
		File portraitFile = model.getPath().getFile("portrait.png");
		try
		{
			if (portraitFile.exists())
			{
				// load portrait as icon
				icon = new ImageIcon(portraitFile.toString());

				// scale icon if too big
				int w = icon.getIconWidth();
				int h = icon.getIconHeight();

				if (w > 100 || h > 80)
				{
					float f1 = 100.0f / w;
					float f2 = 80.0f / h;
					float f = f1 < f2 ? f1 : f2;
					w *= f;
					h *= f;

					Image image = icon.getImage();
					Image newimg = image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
					icon = new ImageIcon(newimg);
				}
			}
		}
		catch (Exception e)
		{
			icon = null;
		}

		// show success message with or without custom icon
		JOptionPane.showMessageDialog(null, sb.toString(), "Generate model", JOptionPane.INFORMATION_MESSAGE, icon);
	}

	private List<AnimationIndex> tryGenerate(String targetFilename) throws PrismException
	{
		// create required MetaBlocks
		IPath_r path = model.getPath();
		MetaBlock devBlock = new MetaBlock(MetaType.DEVELOP, 0);
		MetaBlock modelBlock = new MetaBlock(MetaType.MODEL, 0);

		// keep track of all added animations
		List<AnimationIndex> addedAnimations = new ArrayList<AnimationIndex>();

		// add all available animations
		for (AnimationIndex a : AnimationIndex.values())
		{
			boolean b = addAnimation(path, a, devBlock, modelBlock);
			if (b)
				addedAnimations.add(a);
		}

		// cancel if no animations were found
		if (addedAnimations.size() == 0)
		{
			PrismException e = new PrismException();
			e.setText("No animations could be found. Make sure to generate the model's animations in advance.");
			throw e;
		}

		// add target file
		MetaLine targetLine = new MetaLine(MetaKey.TARGET);
		targetLine.getHeap().texts.add(new Text(targetFilename));
		devBlock.addMetaLine(targetLine);

		// create MetaList
		MetaList metaList = new MetaList();
		metaList.addMetaBlock(modelBlock);
		metaList.addMetaBlock(devBlock);

		// pass information to MetaFileCreator
		MetaFileCreator c = new MetaFileCreator(path, path);
		c.infuseMetaList(metaList);
		c.create();

		// return added animations
		return addedAnimations;
	}

	private boolean addAnimation(IPath_r path, AnimationIndex a, MetaBlock devBlock, MetaBlock modelBlock) throws PrismException
	{
		// generate filenames
		String filename = a.toString().toLowerCase() + ".png";
		File file = path.getFile(filename);
		boolean wasFound = file.exists();

		if (wasFound)
		{
			// load animation meta
			AnimationMeta meta = loadAnimationMeta(path, a);
			for (int i = 0; i < meta.getCount(); ++i)
			{
				MetaLine angleLine = new MetaLine(MetaKey.ITEM);
				angleLine.getHeap().ints.add(meta.getAngle(i).ordinal());
				angleLine.getHeap().ints.add(meta.getColumnCount(i));
				modelBlock.addMetaLine(angleLine);
			}

			// load animation image
			MetaLine attachLine = new MetaLine(MetaKey.ATTACH);
			attachLine.getHeap().texts.add(new Text(filename));
			devBlock.addMetaLine(attachLine);
		}
		return wasFound;
	}

	private AnimationMeta loadAnimationMeta(IPath_r path, AnimationIndex a) throws PrismException
	{
		AnimationMeta meta = null;
		String filename = a.toString().toLowerCase() + ".meta";
		try
		{
			File file = path.getFile(filename);

			meta = new AnimationMeta();
			FileInputStream stream = new FileInputStream(file);
			meta.unpack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was a problem while trying to load the meta file for an animation.");
			e.addInfo("animation", a.toString());
			e.addInfo("meta file", filename);
			throw e;
		}
		return meta;
	}
}
