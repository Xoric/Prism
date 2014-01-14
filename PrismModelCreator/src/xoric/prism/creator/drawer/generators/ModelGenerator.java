package xoric.prism.creator.drawer.generators;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.image.IconLoader;
import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Text;
import xoric.prism.develop.meta.MetaFileCreator;
import xoric.prism.world.animations.AnimationIndex;

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
			// generate model and show success message
			ModelResult result = tryGenerate(targetFilename);
			showSuccess(result);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private void showSuccess(ModelResult result)
	{
		// get filesize
		File targetFile = result.getTargetFile();
		String filename = targetFile.getName();
		String fileSize = Common.getFileSize(targetFile.length());

		// generate message
		StringBuilder sb = new StringBuilder();
		sb.append("<html>Model created successfully.<br><br>");

		final String p1 = "<tr><td>";
		final String p2 = "</td><td><code>";
		final String p3 = "</code></td></tr>";

		sb.append("<table border=\"1\">");
		sb.append(p1 + "File: " + p2 + filename + p3);
		sb.append(p1 + "Size: " + p2 + fileSize + p3);
		sb.append(p1 + "Portrait: " + p2 + (result.hasPortrait() ? "yes" : "no") + p3);
		sb.append(p1 + "Animations: " + p2);

		List<AnimationIndex> addedAnimations = result.getAddedAnimations();
		for (int i = 0; i < addedAnimations.size(); ++i)
		{
			if (i > 0)
				sb.append(", ");

			sb.append(addedAnimations.get(i).toString());
		}
		sb.append(p3);
		sb.append("</table>");
		sb.append("</html>");

		// load the model's portrait as icon if any
		ImageIcon icon = null;
		File portraitFile = model.getPath().getFile("portrait.png");
		try
		{
			// load portrait as icon
			if (portraitFile.exists())
				icon = IconLoader.loadIconFromFile(portraitFile, 140, 140);
		}
		catch (Exception e)
		{
			icon = null;
		}

		// show success message with or without custom icon
		JOptionPane.showMessageDialog(null, sb.toString(), "Generate model", JOptionPane.INFORMATION_MESSAGE, icon);
	}

	private ModelResult tryGenerate(String targetFilename) throws PrismException
	{
		ModelResult result = new ModelResult();

		// create required MetaBlocks
		IPath_r path = model.getPath();
		MetaBlock devBlock = new MetaBlock(MetaType.DEVELOP, 0);
		MetaBlock modelBlock = new MetaBlock(MetaType.MODEL, 0);

		// add portrait if any
		boolean hasPortrait = addPortrait(path, devBlock, modelBlock);
		result.setPortrait(hasPortrait);

		// add all available animations
		for (AnimationIndex a : AnimationIndex.values())
		{
			boolean b = addAnimation(path, a, devBlock, modelBlock);
			if (b)
				result.addAnimation(a);
		}

		// cancel if no animations were found
		if (!result.hasAnimations())
		{
			PrismException e = new PrismException();
			e.setText("No animations could be found. Make sure to generate the model's animations before generating the model.");
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

		// get target file
		File targetFile = c.getResultingTargetFile();
		result.setTargetFile(targetFile);

		return result;
	}

	private boolean addPortrait(IPath_r path, MetaBlock devBlock, MetaBlock modelBlock)
	{
		boolean hasPortrait = false;
		final String filename = "portrait.png";
		File portraitFile = path.getFile(filename);

		if (portraitFile.exists())
		{
			// add portrait line
			MetaLine portraitLine = new MetaLine(MetaKey.ALT);
			modelBlock.addMetaLine(portraitLine);

			// add portrait image
			MetaLine attachLine = new MetaLine(MetaKey.ATTACH);
			attachLine.getHeap().texts.add(new Text(filename));
			devBlock.addMetaLine(attachLine);

			hasPortrait = true;
		}
		return hasPortrait;
	}

	private boolean addAnimation(IPath_r path, AnimationIndex a, MetaBlock devBlock, MetaBlock modelBlock) throws PrismException
	{
		// generate filenames
		String filename = a.toString().toLowerCase() + ".png";
		File file = path.getFile(filename);
		boolean wasFound = file.exists();

		if (wasFound)
		{
			// add animation line
			MetaLine animLine = new MetaLine(MetaKey.ITEM);
			animLine.getHeap().ints.add(a.ordinal());
			modelBlock.addMetaLine(animLine);

			// load animation meta and add all angles
			AnimationMeta meta = loadAnimationMeta(path, a);
			for (int i = 0; i < meta.getCount(); ++i)
			{
				MetaLine angleLine = new MetaLine(MetaKey.SUB);
				angleLine.getHeap().ints.add(meta.getAngle(i).ordinal());
				angleLine.getHeap().ints.add(meta.getColumnCount(i));
				modelBlock.addMetaLine(angleLine);
			}

			// add animation image
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