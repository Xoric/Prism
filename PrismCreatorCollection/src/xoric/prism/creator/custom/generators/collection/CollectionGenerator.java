package xoric.prism.creator.custom.generators.collection;

import java.io.File;
import java.io.FileInputStream;

import xoric.prism.creator.common.factory.SuccessMessage;
import xoric.prism.creator.custom.model.CollectionModel;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Text;
import xoric.prism.develop.meta.MetaFileCreator;

public class CollectionGenerator
{
	private CollectionModel model;

	public CollectionGenerator(CollectionModel model)
	{
		this.model = model;
	}

	public void generateModel()
	{
		try
		{
			// generate model and show success message
			String targetFilename = model.getName().toString().toLowerCase() + ".cl";
			File targetFile = tryGenerate(targetFilename);
			showSuccess(targetFile);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private File tryGenerate(String targetFilename) throws PrismException
	{
		// create dev MetaBlock
		MetaBlock devBlock = new MetaBlock(MetaType.DEVELOP, 0);

		// load collection MetaBlock
		MetaBlock collectionBlock = loadCollectionBlock();

		// make sure texture is there
		ensureTexture();

		// add target file
		MetaLine targetLine = new MetaLine(MetaKey.TARGET);
		targetLine.getHeap().texts.add(new Text(targetFilename));
		devBlock.addMetaLine(targetLine);

		// add texture
		MetaLine attachLine = new MetaLine(MetaKey.ATTACH);
		attachLine.getHeap().texts.add(new Text("TEXTURE.PNG"));
		devBlock.addMetaLine(attachLine);

		// create MetaList
		MetaList metaList = new MetaList();
		metaList.addMetaBlock(collectionBlock);
		metaList.addMetaBlock(devBlock);

		// pass information to MetaFileCreator
		IPath_r path = model.getPath();
		MetaFileCreator c = new MetaFileCreator(path, path);
		c.infuseMetaList(metaList);
		c.create();

		// get target file
		File targetFile = c.getResultingTargetFile();
		return targetFile;
	}

	private MetaBlock loadCollectionBlock() throws PrismException
	{
		File file = model.getPath().getFile("texture.meta");
		MetaBlock collectionBlock;
		try
		{
			collectionBlock = new MetaBlock(MetaType.COLLECTION, 0);
			FileInputStream stream = new FileInputStream(file);
			collectionBlock.unpack(stream);
			stream.close();
		}
		catch (Exception e0)
		{
			PrismException e = new PrismException(e0);
			e.setText("There was an error loading information about the previous generated texture.");
			e.addInfo("file", file.toString());
			throw e;
		}
		return collectionBlock;
	}

	private void ensureTexture() throws PrismException
	{
		File file = model.getPath().getFile("texture.png");
		if (!file.exists())
		{
			PrismException e = new PrismException();
			e.setText("No texture could be found. Generate a texture first.");
			e.addInfo("missing file", file.toString());
			throw e;
		}
	}

	private void showSuccess(File targetFile)
	{
		SuccessMessage m = new SuccessMessage("collection");
		m.addFile(targetFile);
		m.addIcon(model.getPath().getFile("texture.png"));
		m.showMessage();
	}
}
