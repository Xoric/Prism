package xoric.prism.creator.models.generators;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import xoric.prism.creator.common.factory.SuccessMessage;
import xoric.prism.creator.models.model.ModelModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Text;
import xoric.prism.develop.meta.MetaFileCreator;
import xoric.prism.world.animations.AnimationIndex;

public class ModelGenerator
{
	private final ModelModel model;

	public ModelGenerator(ModelModel model)
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
		SuccessMessage m = new SuccessMessage("model");
		m.addFile(result.getTargetFile());
		m.addInfo("Portrait", (result.hasPortrait() ? "yes" : "no"));

		StringBuilder sb = new StringBuilder();
		List<AnimationResult> addedAnimations = result.getAddedAnimations();

		for (int i = 0; i < addedAnimations.size(); ++i)
		{
			if (i > 0)
				sb.append(", ");

			AnimationResult r = addedAnimations.get(i);
			sb.append(r.toString());
		}

		m.addInfo("Animations", sb.toString());
		m.addIcon(model.getPath().getFile("portrait.png"));

		m.showMessage();
	}

	private ModelResult tryGenerate(String targetFilename) throws PrismException
	{
		ModelResult result = new ModelResult();

		// create required MetaBlocks
		IPath_r path = model.getPath();
		MetaBlock devBlock = new MetaBlock(MetaType.DEVELOP, 0);
		MetaBlock modelBlock = new MetaBlock(MetaType.MODEL_G, 0);

		// insert sprite size
		addSpriteSize(modelBlock);

		// add portrait if any
		addPortrait(path, devBlock, modelBlock, result);

		// add all available animations
		for (AnimationIndex a : AnimationIndex.values())
			addAnimation(path, a, devBlock, modelBlock, result);

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

	private void addPortrait(IPath_r path, MetaBlock devBlock, MetaBlock modelBlock, ModelResult result)
	{
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
		}
	}

	private void addSpriteSize(MetaBlock modelBlock)
	{
		MetaLine sizeLine = new MetaLine(MetaKey.SIZE);
		sizeLine.getHeap().ints.add(model.getSpriteSize().getX());
		sizeLine.getHeap().ints.add(model.getSpriteSize().getY());
		modelBlock.insertMetaLine(0, sizeLine);
	}

	private void addAnimation(IPath_r path, AnimationIndex a, MetaBlock devBlock, MetaBlock modelBlock, ModelResult result)
			throws PrismException
	{
		int variation = 0;
		boolean b;

		do
		{
			b = addVariation(path, a, variation, devBlock, modelBlock);
			if (b)
			{
				result.addAnimation(a);
				++variation;
			}
		}
		while (b);
	}

	private boolean addVariation(IPath_r path, AnimationIndex a, int variation, MetaBlock devBlock, MetaBlock modelBlock)
			throws PrismException
	{
		// generate filenames
		String filename = a.toString().toLowerCase() + variation + ".png";
		File file = path.getFile(filename);
		boolean wasFound = file.exists();

		if (wasFound)
		{
			// add animation line
			MetaLine animLine = new MetaLine(MetaKey.ITEM);
			animLine.getHeap().ints.add(a.ordinal());
			modelBlock.addMetaLine(animLine);

			// load animation meta and add all angles
			AnimationMeta meta = loadVariationMeta(path, a, variation);
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

	private AnimationMeta loadVariationMeta(IPath_r path, AnimationIndex a, int variation) throws PrismException
	{
		AnimationMeta meta = null;
		String filename = a.toString().toLowerCase() + variation + ".meta";
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
			e.addInfo("variation", variation);
			e.addInfo("meta file", filename);
			throw e;
		}
		return meta;
	}
}
